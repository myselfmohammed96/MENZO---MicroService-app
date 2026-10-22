package com.menzo.User_Service.Global.FeignClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.OtpServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class CommunicationErrorDecoder implements ErrorDecoder {

    private static final Set<ErrorCode> PASS_THROUGH = EnumSet.of(
            ErrorCode.OTP_RATE_LIMITED, ErrorCode.OTP_INVALID, ErrorCode.OTP_EXPIRED,
            ErrorCode.OTP_NOT_FOUND, ErrorCode.OTP_VERIFY_LIMIT_EXCEEDED);

    static final String SEND_FAILED_MESSAGE =
            "We couldn't send the verification code right now. Your account is saved; please request a new code in a moment.";
    static final String VERIFY_UNAVAILABLE_MESSAGE =
            "We couldn't verify your code right now. Please try again in a moment.";

    private final ObjectMapper objectMapper;

    public CommunicationErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String upstreamCode = null;
        String upstreamMessage = null;
        Map<String, String> upstreamErrors = new LinkedHashMap<>();
        if (response.body() != null) {
            try (InputStream in = response.body().asInputStream()) {
                JsonNode node = objectMapper.readTree(in);
                upstreamCode = node.path("code").asText(null);
                upstreamMessage = node.path("message").asText(null);
            } catch (IOException e) {
                log.warn("Could not parse error body from {}", methodKey);
            }
        }
        log.warn("communication-service call {} failed: status={}, code={}", methodKey, response.status(), upstreamCode);

        ErrorCode mapped = resolve(upstreamCode);
        if (mapped == null && response.status() == 429) {
            mapped = ErrorCode.OTP_RATE_LIMITED;
        }
        if (mapped != null && PASS_THROUGH.contains(mapped)) {
            String message = upstreamMessage != null ? upstreamMessage : "The OTP request could not be completed.";
            return new OtpServiceException(mapped, message, upstreamErrors).withRetryAfter(retryAfter(response));
        }

        return methodKey.contains("verifyOtp")
                ? new OtpServiceException(ErrorCode.OTP_VERIFY_UNAVAILABLE, VERIFY_UNAVAILABLE_MESSAGE)
                : new OtpServiceException(ErrorCode.OTP_SEND_FAILED, SEND_FAILED_MESSAGE);
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
