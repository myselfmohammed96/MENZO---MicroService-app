package com.menzo.User_Service.Exceptions;

public class ResourceAlreadyDeletedException extends RuntimeException {

    public ResourceAlreadyDeletedException() {
        super("Resource already exists.");
    }

    public ResourceAlreadyDeletedException(String message) {
        super(message);
    }

    public ResourceAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
