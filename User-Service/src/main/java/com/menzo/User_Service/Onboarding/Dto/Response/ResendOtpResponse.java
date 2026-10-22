package com.menzo.User_Service.Onboarding.Dto.Response;

public record ResendOtpResponse(String email, long otpExpiresInSeconds, long resendAfterSeconds) {
}
