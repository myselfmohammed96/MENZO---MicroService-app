package com.menzo.User_Service.Onboarding.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyOtpRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100, message = "Email must not exceed 100 characters.")
        String email,

        @NotBlank(message = "OTP is required.")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits.")
        String otp
) {
    @Override
    public String toString() {
        return "VerifyOtpRequest[email=" + email + ", otp=******]";
    }
}
