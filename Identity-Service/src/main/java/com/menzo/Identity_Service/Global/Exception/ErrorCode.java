package com.menzo.Identity_Service.Global.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public enum ErrorCode {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST),
    MALFORMED_REQUEST(HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    UNAUTHORIZED_INTERNAL_CALL(HttpStatus.UNAUTHORIZED),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_VERIFIED(HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN),
    ACCOUNT_USES_SOCIAL_LOGIN(HttpStatus.BAD_REQUEST),
    TOO_MANY_LOGIN_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS),

    GOOGLE_TOKEN_INVALID(HttpStatus.UNAUTHORIZED),
    GOOGLE_VERIFICATION_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    TOKEN_USER_MISMATCH(HttpStatus.CONFLICT),
    USER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }
}
