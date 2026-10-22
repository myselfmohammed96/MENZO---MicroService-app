package com.menzo.Identity_Service.Global.Exception;

public class SocialLoginOnlyException extends RuntimeException {
    public SocialLoginOnlyException() {
        super("Account has no password");
    }
}
