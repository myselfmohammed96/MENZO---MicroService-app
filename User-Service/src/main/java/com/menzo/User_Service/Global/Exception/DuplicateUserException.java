package com.menzo.User_Service.Global.Exception;

public class DuplicateUserException extends BusinessException {

    public DuplicateUserException(ErrorCode code, String message) {
        super(code, message);
    }
}
