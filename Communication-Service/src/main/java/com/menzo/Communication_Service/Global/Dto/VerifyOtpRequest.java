package com.menzo.Communication_Service.Global.Dto;

import com.menzo.Communication_Service.Global.Enum.Purpose;
import jakarta.validation.constraints.*;

public record VerifyOtpRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100, message = "Email must not exceed 100 characters.")
        String email,

        @NotBlank(message = "OTP is required.")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits.")
        String otp,

        @NotNull(message = "Purpose is required.")
        Purpose purpose
) {
}
