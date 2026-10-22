package com.menzo.User_Service.Global.Exception;

public class AuthServiceException extends BusinessException {

    public AuthServiceException(ErrorCode code, String message) {
        super(code, message);
    }

    public AuthServiceException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
