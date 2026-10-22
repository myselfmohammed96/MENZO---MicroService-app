package com.menzo.User_Service.Onboarding.Dto.Response;

public record CredentialsResponse(
        String userId,
        String email,
        String passwordHash,
        String role,
        boolean active,
        boolean verified,
        String registrationSource
) {
    @Override
    public String toString() {
        return "CredentialsResponse[userId=" + userId + ", role=" + role + ", active=" + active + ", verified=" + verified + "]";
    }
}
