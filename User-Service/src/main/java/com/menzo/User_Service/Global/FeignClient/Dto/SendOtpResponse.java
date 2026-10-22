package com.menzo.User_Service.Global.FeignClient.Dto;

public record SendOtpResponse(String status, long expiresInSeconds, long resendAfterSeconds) {
}
