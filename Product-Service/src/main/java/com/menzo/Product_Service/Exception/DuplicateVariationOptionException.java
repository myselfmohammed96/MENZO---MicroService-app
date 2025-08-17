package com.menzo.Product_Service.Exception;

public class DuplicateVariationOptionException extends RuntimeException{

    public DuplicateVariationOptionException(String message) {
        super(message);
    }

    public DuplicateVariationOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
