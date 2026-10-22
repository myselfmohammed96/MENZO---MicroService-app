package com.menzo.Identity_Service.Auth.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GoogleVerifyRequest(
        @NotBlank(message = "idToken is required.")
        @Size(max = 4096)
        String idToken
) {
    @Override
    public String toString() {
        return "GoogleVerifyRequest[idToken=******]";
    }
}
