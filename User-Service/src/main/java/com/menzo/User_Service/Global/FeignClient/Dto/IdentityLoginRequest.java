package com.menzo.User_Service.Global.FeignClient.Dto;

public record IdentityLoginRequest(String email, String password) {

    @Override
    public String toString() {
        return "IdentityLoginRequest[email=" + email + ", password=******]";
    }
}
