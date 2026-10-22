package com.menzo.User_Service.Global.Exception.ExceptionsDep;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

}
