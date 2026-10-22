package com.menzo.User_Service.Global.Exception;

import java.util.Map;

public class RequestValidationException extends BusinessException {

    public RequestValidationException(Map<String, String> fieldErrors) {
        super(ErrorCode.VALIDATION_FAILED, "Validation failed. Please correct the highlighted fields.", fieldErrors);
    }

}
