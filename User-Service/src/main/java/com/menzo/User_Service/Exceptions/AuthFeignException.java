package com.menzo.User_Service.Exceptions;

public class AuthFeignException extends RuntimeException{

    private final int status;

    public AuthFeignException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
