package com.menzo.User_Service.Exceptions;

public class DuplicateAddressException extends RuntimeException {

    public DuplicateAddressException() {
        super("Address already exists.");
    }

    public DuplicateAddressException(String message) {
        super(message);
    }

    public DuplicateAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
