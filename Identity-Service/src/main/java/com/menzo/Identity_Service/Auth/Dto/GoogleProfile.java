package com.menzo.Identity_Service.Auth.Dto;

public record GoogleProfile(
        String sub,
        String email,
        boolean emailVerified,
        String giveName,
        String familyName,
        String picture
) {
}
