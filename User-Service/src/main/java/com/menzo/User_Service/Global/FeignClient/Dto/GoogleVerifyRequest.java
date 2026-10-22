package com.menzo.User_Service.Global.FeignClient.Dto;

public record GoogleVerifyRequest(String idToken) {
    @Override
    public String toString() {
        return "GoogleVerifyRequest[idToken=******]";
    }
}
