package com.menzo.Product_Service.Exception;

public class DuplicateVariationException extends RuntimeException{

    public DuplicateVariationException(String message) {
        super(message);
    }

    public DuplicateVariationException(String message, Throwable cause) {
        super(message, cause);
    }
}
