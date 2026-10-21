package com.menzo.Communication_Service.Global.Enum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST),
    MALFORMED_REQUEST(HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    OTP_RATE_LIMITED(HttpStatus.TOO_MANY_REQUESTS),
    OTP_INVALID(HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(HttpStatus.BAD_REQUEST),
    OTP_VERIFY_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS),
    CONCURRENT_REQUEST(HttpStatus.CONFLICT),
    EMAIL_DELIVERY_FAILED(HttpStatus.BAD_GATEWAY),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }
}
