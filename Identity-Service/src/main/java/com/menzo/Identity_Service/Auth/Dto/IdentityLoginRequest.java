package com.menzo.Identity_Service.Auth.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdentityLoginRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Password is required.")
        @Size(max = 100)
        String password
) {
    @Override
    public String toString() {
        return "IdentityLoginRequest[email=" + email + ", password=******]";
    }
}
