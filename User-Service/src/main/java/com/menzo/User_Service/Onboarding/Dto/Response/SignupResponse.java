package com.menzo.User_Service.Onboarding.Dto.Response;

public record SignupResponse(
        String email,
//        String redirectUrl,
        long otpExpiresInSeconds,
        long resendAfterSeconds
) {
}
