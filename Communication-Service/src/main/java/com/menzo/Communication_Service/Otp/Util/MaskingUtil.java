package com.menzo.Communication_Service.Otp.Util;

public final class MaskingUtil {

    private MaskingUtil() {}

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }

        int at = email.indexOf('@');
        String visible = at > 0 ? email.substring(0, 1) : "";
        return visible + "***" + email.substring(at);
    }
}
