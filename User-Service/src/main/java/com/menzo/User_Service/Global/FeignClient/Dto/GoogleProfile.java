package com.menzo.User_Service.Global.FeignClient.Dto;

public record GoogleProfile(
        String sub,
        String email,
        boolean emailVerified,
        String givenName,
        String familyName,
        String picture
) {
}
