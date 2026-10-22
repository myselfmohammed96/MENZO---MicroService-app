package com.menzo.User_Service.Onboarding.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100, message = "Email must not exceed 100 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(max = 100, message = "Password must not exceed 100 characters")
        String password,

        @Size(max = 2048, message = "Redirect URL is too long.")
        String redirectUrl
) {
    @Override
    public String toString() {
        return "LoginRequest[email=" + email + ", password=******, redirectUrl=" + redirectUrl + "]";
    }
}
