package com.menzo.User_Service.Global.FeignClient.Dto;

public record OtpVerifyRequest(String email, String otp, OtpPurpose purpose) {
}
