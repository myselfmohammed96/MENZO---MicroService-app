package com.menzo.User_Service.Onboarding.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GoogleLoginRequest(
        @NotBlank(message = "Google ID token is required.")
        @Size(max = 4096, message = "Google ID token is too long.")
        String idToken,

        @Size(max = 2048, message = "Redirect URL is too long.")
        String redirectUrl
) {
    @Override
    public String toString() {
        return "GoogleLoginRequest[idToken=******, redirectUrl=" + redirectUrl + "]";
    }
}
