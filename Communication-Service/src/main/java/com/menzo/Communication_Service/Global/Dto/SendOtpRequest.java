package com.menzo.Communication_Service.Global.Dto;

import com.menzo.Communication_Service.Global.Enum.Purpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendOtpRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email address.")
        @Size(max = 100, message = "Email must not exceed 100 characters.")
        String email,

        @NotBlank(message = "First name is required.")
        @Size(max = 50, message = "First name must be at most 50 characters.")
        String firstName,

        @NotNull(message = "Purpose is required")
        Purpose purpose
) {
}
