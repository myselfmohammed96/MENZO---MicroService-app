package com.menzo.Identity_Service.Auth.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IssueTokenRequest(
        @NotBlank(message = "userId is required.") @Size(max = 64) String userId,
        @NotBlank(message = "email is required.") @Email @Size(max = 100) String email,
        @Size(max = 50) String role,
        @NotNull(message = "authMethod is required.") AuthMethod authMethod
) {
}
