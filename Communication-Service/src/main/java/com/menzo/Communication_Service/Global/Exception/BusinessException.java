package com.menzo.Communication_Service.Global.Exception;

import com.menzo.Communication_Service.Global.Enum.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode code;
    private Map<String, String> errors = Map.of();
    private Long retryAfterSeconds;

    public BusinessException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException withRetryAfter(Long seconds) {
        this.retryAfterSeconds = seconds;
        return this;
    }

    public BusinessException withErrors(Map<String, String> errors) {
        this.errors = errors == null ? Map.of() : errors;
        return this;
    }

}
