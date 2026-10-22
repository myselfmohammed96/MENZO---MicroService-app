package com.menzo.User_Service.Onboarding.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResendOtpRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100, message = "Email must not exceed 100 characters.")
        String email
) {
}
