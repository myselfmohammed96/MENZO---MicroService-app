package com.menzo.Identity_Service.Global.Exception;

public class UserServiceUnavailableException extends RuntimeException {
    public UserServiceUnavailableException(Throwable cause) {
        super("user-service unavailable", cause);
    }
}
