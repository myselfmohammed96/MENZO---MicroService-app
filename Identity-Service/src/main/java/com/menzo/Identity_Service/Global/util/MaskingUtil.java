package com.menzo.Identity_Service.Global.util;

public class MaskingUtil {

    private MaskingUtil() {
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        int at = email.indexOf('@');
        return (at > 0 ? email.substring(0, 1) : "") + "***" + email.substring(at);
    }

}
