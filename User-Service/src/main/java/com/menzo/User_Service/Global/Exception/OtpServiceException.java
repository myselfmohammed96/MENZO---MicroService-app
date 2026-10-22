package com.menzo.User_Service.Global.Exception;

import java.util.Map;

public class OtpServiceException extends BusinessException {

    public OtpServiceException(ErrorCode code, String message) {
        super(code, message);
    }

    public OtpServiceException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public OtpServiceException(ErrorCode code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }
}
