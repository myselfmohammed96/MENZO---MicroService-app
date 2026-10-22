package com.menzo.User_Service.Global.Exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode code;
    private final Map<String, String> errors;
    private Long retryAfterSeconds;

    public BusinessException(ErrorCode code, String message) {
        super(message);
        this.code = code;
        this.errors = Map.of();
    }

    public BusinessException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errors = Map.of();
    }

    public BusinessException(ErrorCode code, String message, Map<String, String> errors) {
        super(message);
        this.code = code;
        this.errors = errors == null ? Map.of() : errors;
    }

    public BusinessException withRetryAfter(Long seconds) {
        this.retryAfterSeconds = seconds;
        return this;
    }
}

