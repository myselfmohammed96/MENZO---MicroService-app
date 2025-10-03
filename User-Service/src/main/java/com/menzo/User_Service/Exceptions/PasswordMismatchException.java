package com.menzo.User_Service.Exceptions;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("The current password doesn't match.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
