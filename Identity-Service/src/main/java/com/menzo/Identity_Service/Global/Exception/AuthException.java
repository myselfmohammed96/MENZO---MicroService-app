package com.menzo.Identity_Service.Global.Exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final ErrorCode code;
    private Long retryAfterSeconds;

    public AuthException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException withRetryAfter(Long seconds) {
        this.retryAfterSeconds = seconds;
        return this;
    }
}
