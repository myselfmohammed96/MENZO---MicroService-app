package com.menzo.User_Service.Global.Exception.ExceptionsDep;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException() {
        super("Entity already exists.");
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
