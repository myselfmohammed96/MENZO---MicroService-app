package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Cache.DirectedTo;
import com.menzo.User_Service.Onboarding.Dto.ResendOtpRequest;
//import com.menzo.User_Service.Global.Dto.Response.AuthResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResult;
//import com.menzo.User_Service.Global.Dto.Response.ResendOtpResponse;
//import com.menzo.User_Service.Global.Dto.Response.VerifyOtpResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResult;
import com.menzo.User_Service.Onboarding.Dto.Response.ResendOtpResponse;
import com.menzo.User_Service.Onboarding.Dto.VerifyOtpRequest;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.AuthServiceException;
import com.menzo.User_Service.Global.Exception.BusinessException;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import com.menzo.User_Service.Global.FeignClient.Dto.AuthMethod;
import com.menzo.User_Service.Global.FeignClient.Dto.SendOtpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserRepository userRepo;
    private final InputSanitizer sanitizer;
    private final OtpGatewayService otpGatewayService;
    private final UserActivationService activationService;
    private final RedirectUrlService redirectUrlService;
    private final IdentityGatewayService identityGateway;

    public AuthResult verifyOtp(VerifyOtpRequest request) {
        String email = sanitizer.normalizeEmail(request.email());
        String masked = MaskingUtil.maskEmail(email);
        log.info("Verify OTP requested for {}", masked);

        findPendingUser(email);
        otpGatewayService.verifySignupOtp(email, request.otp());

        User user = activationService.activate(email);
        String redirect = redirectUrlService.consume(email, DirectedTo.SIGN_UP);

//        otpGatewayService.verifySignupOtp(email, request.otp());   // throws with a precise reason if invalid

        try {
            IssuedToken token = identityGateway.issueToken(user, AuthMethod.SIGNUP_OTP);
            log.info("Registration completed, userId={}", user.getUserId());
            return new AuthResult(AuthResponse.from(user, redirect, true, true), token.setCookies(), true);
        } catch (AuthServiceException ex) {
            // Registration itself succeeded. Don't fail the request: tell the client to sign in manually.
            log.error("userId={} registered but the session could not be issued ({})", user.getUserId(), ex.getCode());
            return new AuthResult(AuthResponse.from(user, redirect, false, true), List.of(), false);
        }
//        user.setVerified(true);
//        userRepo.save(user);
//        log.info("User verified, userId={}", user.getUserId());
//        return new VerifyOtpResponse(email, true);
    }

    public ResendOtpResponse resendOtp(ResendOtpRequest request) {
        String email = sanitizer.normalizeEmail(request.email());
        log.info("Resend OTP requested for {}", MaskingUtil.maskEmail(email));

        User user = findPendingUser(email);

        // communication-service enforces cooldown, hourly send cap and block window (-> 429 + Retry-After)
        SendOtpResponse otp = otpGatewayService.sendSignupOtp(user.getEmail(), user.getFirstName());
        return new ResendOtpResponse(email, otp.expiresInSeconds(), otp.resendAfterSeconds());
    }

    private User findPendingUser(String email) {
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> {
            log.warn("No user found for {}", MaskingUtil.maskEmail(email));
            return new BusinessException(ErrorCode.USER_NOT_FOUND, "No signup found for this email. Please register first.");
        });
        if (user.isVerified()) {
            log.warn("Email already verified: {}", MaskingUtil.maskEmail(email));
            throw new BusinessException(ErrorCode.ALREADY_VERIFIED, "This email is already verified. Please sign in.");
        }
        return user;
    }
}
