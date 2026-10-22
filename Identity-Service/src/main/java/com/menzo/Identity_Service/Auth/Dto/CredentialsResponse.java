package com.menzo.Identity_Service.Auth.Dto;

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
        return "CredentialsResponse[userId=" + userId +
                ", role=" + role +
                ", active=" + active +
                ", verified=" + verified + "]";
    }
}
