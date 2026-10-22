package com.menzo.User_Service.Onboarding.Service;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.AuthServiceException;
import com.menzo.User_Service.Global.FeignClient.Dto.*;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.Global.FeignClient.IdentityClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityGatewayService {

    private static final String UNAVAILABLE = "Sign-in is temporarily unavailable. Please try again in a moment.";

    private final IdentityClient identityClient;

    public IssuedToken issueToken(User user, AuthMethod method) {
        log.info("Requesting session from identity-service, userId={}, method={}", user.getUserId(), method);
        return call("issueToken", () -> toIssued(identityClient.issueToken(new IssueTokenRequest(
                String.valueOf(user.getUserId()), user.getEmail(), user.getUserType().name(), method))));
    }

    public IssuedToken login(String email, String password) {
        log.info("Delegating login to identity-service for {}", MaskingUtil.maskEmail(email));
        return call("login", () -> toIssued(identityClient.login(new IdentityLoginRequest(email, password))));
    }

    public GoogleProfile verifyGoogleToken(String idToken) {
        return call("verifyGoogleToken", () -> {
            ApiResponse<GoogleProfile> response = identityClient.verifyGoogleToken(new GoogleVerifyRequest(idToken));
            if (response == null || !response.success() || response.data() == null) {
                throw new AuthServiceException(ErrorCode.GOOGLE_TOKEN_INVALID, "Google sign-in failed. Please try again.");
            }
            return response.data();
        });
    }

    private IssuedToken toIssued(ResponseEntity<ApiResponse<IdentityAuthData>> response) {
        ApiResponse<IdentityAuthData> body = response.getBody();
        List<String> cookies = response.getHeaders().getOrEmpty(HttpHeaders.SET_COOKIE);
        if (body == null || !body.success() || body.data() == null || cookies.isEmpty()) {
            log.error("identity-service answered 2xx but without a usable session (body={}, cookies={})",
                    body != null, cookies.size());
            throw new AuthServiceException(ErrorCode.AUTH_SERVICE_UNAVAILABLE, UNAVAILABLE);
        }
        return new IssuedToken(body.data(), List.copyOf(cookies));
    }

    private <T> T call(String operation, Supplier<T> action) {
        try {
            return action.get();
        } catch (AuthServiceException ex) {
            throw ex;                                   // already mapped by the error decoder / checks above
        } catch (RuntimeException ex) {                 // timeouts, connection refused, no Eureka instance, bad JSON...
            log.error("identity-service {} failed", operation, ex);
            throw new AuthServiceException(ErrorCode.AUTH_SERVICE_UNAVAILABLE, UNAVAILABLE, ex);
        }
    }
}
