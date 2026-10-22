package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Auth.Dto.AuthMethod;
import com.menzo.Identity_Service.Auth.Dto.IdentityAuthData;
import com.menzo.Identity_Service.Auth.Dto.IdentityLoginRequest;
import com.menzo.Identity_Service.Auth.Dto.IssueTokenRequest;
import com.menzo.Identity_Service.Global.Exception.AuthException;
import com.menzo.Identity_Service.Global.Exception.ErrorCode;
import com.menzo.Identity_Service.Global.Exception.SocialLoginOnlyException;
import com.menzo.Identity_Service.Global.Exception.UserServiceUnavailableException;
import com.menzo.Identity_Service.Global.Security.AuthUser;
import com.menzo.Identity_Service.Global.Security.CredentialsUserDetailsService;
import com.menzo.Identity_Service.Global.util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityAuthService {

    private static final String BAD_CREDENTIALS = "Invalid email or password.";

    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttempts;
    private final CredentialsGateway credentialsGateway;
    private final JwtService jwtService;
    private final AuthCookieFactory cookieFactory;
    private final TokenRevocationService revocationService;

    public record IssuedSession(IdentityAuthData data, String setCookie) {
    }

    /** Email + password through Spring Security, with lockout and post-password account checks. */
    public IssuedSession login(IdentityLoginRequest request) {
        String email = normalize(request.email());
        String masked = MaskingUtil.maskEmail(email);

        loginAttempts.assertNotLocked(email);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, request.password()));
        } catch (BadCredentialsException ex) {                         // wrong password OR unknown email: indistinguishable
            recordFailureQuietly(email);
            log.warn("Login failed for {}", masked);
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS, BAD_CREDENTIALS);
        } catch (InternalAuthenticationServiceException ex) {
            if (ex.getCause() instanceof SocialLoginOnlyException) {
                log.info("Password login attempted on a social-only account: {}", masked);
                throw new AuthException(ErrorCode.ACCOUNT_USES_SOCIAL_LOGIN,
                        "This account uses Google sign-in. Please continue with Google.");
            }
            if (ex.getCause() instanceof UserServiceUnavailableException) {
                throw (UserServiceUnavailableException) ex.getCause();
            }
            log.error("Authentication infrastructure failure for {}", masked, ex);
            throw new AuthException(ErrorCode.USER_SERVICE_UNAVAILABLE, "Sign-in is temporarily unavailable. Please try again shortly.");
        } catch (AuthenticationException ex) {
            recordFailureQuietly(email);
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS, BAD_CREDENTIALS);
        }

        AuthUser user = (AuthUser) authentication.getPrincipal();
        // Password is proven now, so it is safe to tell the user about the account state.
        assertUsable(user);

        loginAttempts.reset(email);
        log.info("Login successful, userId={}", user.getUserId());
        return issue(user, AuthMethod.PASSWORD);
    }

    /**
     * Session for a user that user-service has ALREADY authenticated (OTP / Google / password reset).
     * The role and status are re-read from user-service, never trusted from the request.
     */
    public IssuedSession issueToken(IssueTokenRequest request) {
        if (request.authMethod() == AuthMethod.PASSWORD) {
            throw new AuthException(ErrorCode.VALIDATION_FAILED, "Password sessions are issued through /login.");
        }
        UUID userId;
        try {
            userId = UUID.fromString(request.userId());
        } catch (IllegalArgumentException ex) {
            throw new AuthException(ErrorCode.VALIDATION_FAILED, "userId must be a valid UUID.");
        }

        String email = normalize(request.email());
        AuthUser user = credentialsGateway.find(email)
                .map(CredentialsUserDetailsService::toAuthUser)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND, "User not found."));
        if (!user.getUserId().equalsIgnoreCase(userId.toString())) {
            log.error("Token request userId does not match the account for {}", MaskingUtil.maskEmail(email));
            throw new AuthException(ErrorCode.TOKEN_USER_MISMATCH, "Token request does not match the account.");
        }
        assertUsable(user);

        if (request.authMethod() == AuthMethod.PASSWORD_RESET) {
            revocationService.revokeAllFor(userId);                    // sign out every other session
        }
        log.info("Session issued, userId={}, method={}", user.getUserId(), request.authMethod());
        return issue(user, request.authMethod());
    }

    private void assertUsable(AuthUser user) {
        if (!user.isActive()) {
            throw new AuthException(ErrorCode.ACCOUNT_DISABLED, "This account has been disabled. Please contact support.");
        }
        if (!user.isVerified()) {
            throw new AuthException(ErrorCode.ACCOUNT_NOT_VERIFIED, "Your email is not verified yet. Please verify it to continue.");
        }
    }

    private IssuedSession issue(AuthUser user, AuthMethod method) {
        String jwt = jwtService.generate(user.getUserId(), user.getEmail(), List.of(user.getRole()), method);
        return new IssuedSession(new IdentityAuthData(user.getUserId(), user.getEmail(), user.getRole()),
                cookieFactory.issue(jwt));
    }

    /** A failure to count must never mask the real answer. */
    private void recordFailureQuietly(String email) {
        try {
            loginAttempts.recordFailure(email);
        } catch (RuntimeException ex) {
            log.warn("Could not record failed login for {}", MaskingUtil.maskEmail(email), ex);
        }
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
