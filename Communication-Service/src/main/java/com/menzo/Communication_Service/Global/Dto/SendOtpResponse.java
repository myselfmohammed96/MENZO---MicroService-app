package com.menzo.Communication_Service.Global.Dto;

public record SendOtpResponse(String status,
                              long expiresInSeconds,
                              long resendAfterSeconds) {
}
