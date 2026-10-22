package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Config.SignupProperties;
//import com.menzo.User_Service.Global.Dto.GoogleLoginRequest;
//import com.menzo.User_Service.Global.Dto.LoginRequest;
//import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResult;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.BusinessException;
import com.menzo.User_Service.Global.Exception.RequestValidationException;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.Onboarding.Dto.GoogleLoginRequest;
import com.menzo.User_Service.Onboarding.Dto.LoginRequest;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResult;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import com.menzo.User_Service.Global.FeignClient.Dto.AuthMethod;
import com.menzo.User_Service.Global.FeignClient.Dto.GoogleProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepo;
    private final InputSanitizer sanitizer;
    private final SignupProperties signupProperties;
    private final IdentityGatewayService identityGateway;
    private final SocialUserService socialUserService;

    public AuthResult login(LoginRequest request) {
        String email = sanitizer.normalizeEmail(request.email());
        String masked = MaskingUtil.maskEmail(email);
        String redirect = resolveRedirect(request.redirectUrl());
        log.info("Login attempt for {}", masked);

        // identity-service authenticates (Spring Security): bad password, unverified, disabled, throttled -> typed errors
        IssuedToken token = identityGateway.login(email, request.password());

        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> {
            log.error("identity-service authenticated {} but no such user exists here (data inconsistency)", masked);
            return new BusinessException(ErrorCode.INTERNAL_ERROR, "Something went wrong on our side. Please try again later.");
        });

        log.info("Login successful, userId={}", user.getUserId());
        return new AuthResult(AuthResponse.from(user, redirect, true, false), token.setCookies(), true);
    }

    public AuthResult googleLogin(GoogleLoginRequest request) {
        String redirect = resolveRedirect(request.redirectUrl());

        GoogleProfile profile = identityGateway.verifyGoogleToken(request.idToken());
        if (!profile.emailVerified() || profile.email() == null || profile.email().isBlank()) {
            log.warn("Google login rejected: email not verified by Google");
            throw new BusinessException(ErrorCode.GOOGLE_EMAIL_NOT_VERIFIED,
                    "Your Google account email is not verified. Please verify it with Google and try again.");
        }
        String masked = MaskingUtil.maskEmail(profile.email());
        log.info("Google login for {}", masked);

        SocialUserService.Result result;
        try {
            result = socialUserService.findOrCreateGoogleUser(profile);
        } catch (DataIntegrityViolationException ex) {      // two first-time logins raced; the winner's row exists now
            log.warn("Concurrent first Google login for {}, retrying once", masked);
            result = socialUserService.findOrCreateGoogleUser(profile);
        }

        IssuedToken token = identityGateway.issueToken(result.user(), AuthMethod.GOOGLE);
        log.info("Google login successful, userId={}, newUser={}", result.user().getUserId(), result.created());
        return new AuthResult(AuthResponse.from(result.user(), redirect, true, result.created()), token.setCookies(), true);
    }

    /** Client-supplied redirects are returned to the browser, so they must pass the open-redirect checks. */
    private String resolveRedirect(String raw) {
        String cleaned = sanitizer.cleanText(raw);
        if (cleaned == null || cleaned.isEmpty()) {
            return signupProperties.defaultRedirectUrl();
        }
        if (!sanitizer.isAllowedRedirect(cleaned)) {
            throw new RequestValidationException(Map.of("redirectUrl", "Redirect URL is not allowed."));
        }
        return cleaned;
    }
}
