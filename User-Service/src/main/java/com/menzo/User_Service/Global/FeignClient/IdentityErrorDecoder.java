package com.menzo.User_Service.Global.FeignClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.AuthServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@Slf4j
public class IdentityErrorDecoder implements ErrorDecoder {

    private static final Set<ErrorCode> PASS_THROUGH = EnumSet.of(
            ErrorCode.INVALID_CREDENTIALS, ErrorCode.ACCOUNT_NOT_VERIFIED, ErrorCode.ACCOUNT_DISABLED,
            ErrorCode.ACCOUNT_USES_SOCIAL_LOGIN, ErrorCode.TOO_MANY_LOGIN_ATTEMPTS, ErrorCode.GOOGLE_TOKEN_INVALID);

    static final String UNAVAILABLE_MESSAGE = "Sign-in is temporarily unavailable. Please try again in a moment.";

    private final ObjectMapper objectMapper;

    public IdentityErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String upstreamCode = null;
        String upstreamMessage = null;
        if (response.body() != null) {
            try (InputStream in = response.body().asInputStream()) {
                JsonNode node = objectMapper.readTree(in);
                upstreamCode = node.path("code").asText(null);
                upstreamMessage = node.path("message").asText(null);
            } catch (IOException e) {
                log.warn("Could not parse error body from {}", methodKey);
            }
        }
        log.warn("identity-service call {} failed: status={}, code={}", methodKey, response.status(), upstreamCode);

        ErrorCode mapped = resolve(upstreamCode);
        if (mapped == null) {                                  // upstream didn't use our envelope (e.g. raw Spring Security 401)
            mapped = switch (response.status()) {
                case 401 -> methodKey.contains("verifyGoogleToken") ? ErrorCode.GOOGLE_TOKEN_INVALID
                        : methodKey.contains("login") ? ErrorCode.INVALID_CREDENTIALS : null;
                case 429 -> ErrorCode.TOO_MANY_LOGIN_ATTEMPTS;
                default -> null;
            };
        }
        if (mapped != null && PASS_THROUGH.contains(mapped)) {
            String message = upstreamMessage != null ? upstreamMessage : defaultMessage(mapped);
            return new AuthServiceException(mapped, message).withRetryAfter(retryAfter(response));
        }
        return new AuthServiceException(ErrorCode.AUTH_SERVICE_UNAVAILABLE, UNAVAILABLE_MESSAGE);
    }

    private String defaultMessage(ErrorCode code) {
        return switch (code) {
            case INVALID_CREDENTIALS -> "Invalid email or password.";
            case ACCOUNT_NOT_VERIFIED -> "Your email is not verified yet. Please verify it to continue.";
            case ACCOUNT_DISABLED -> "This account has been disabled. Please contact support.";
            case ACCOUNT_USES_SOCIAL_LOGIN -> "This account uses Google sign-in. Please continue with Google.";
            case TOO_MANY_LOGIN_ATTEMPTS -> "Too many login attempts. Please try again later.";
            case GOOGLE_TOKEN_INVALID -> "Google sign-in failed. Please try again.";
            default -> UNAVAILABLE_MESSAGE;
        };
    }

    private ErrorCode resolve(String code) {
        if (code == null) {
            return null;
        }
        try {
            return ErrorCode.valueOf(code);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Long retryAfter(Response response) {
        Collection<String> values = response.headers().get("Retry-After");
        if (values == null || values.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(values.iterator().next().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
