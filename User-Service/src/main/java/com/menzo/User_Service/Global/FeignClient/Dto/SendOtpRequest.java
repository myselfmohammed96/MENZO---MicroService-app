package com.menzo.User_Service.Global.FeignClient.Dto;

public record SendOtpRequest(String email, String firstName, OtpPurpose purpose) {
}
