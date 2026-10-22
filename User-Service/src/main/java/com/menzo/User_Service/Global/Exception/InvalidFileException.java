package com.menzo.User_Service.Global.Exception;

public class InvalidFileException extends BusinessException {

    public InvalidFileException(ErrorCode code, String message) {
        super(code, message);
    }
}
