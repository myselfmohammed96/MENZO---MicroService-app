package com.menzo.User_Service.Onboarding.Dto.Response;

import com.menzo.User_Service.User.Profile.Entity.User;

public record AuthResponse(
        String userId,
        String email,
        String firstName,
        String lastName,
        String profileUrl,
        String redirectUrl,
        boolean authenticated,
        boolean newUser
) {

    public static AuthResponse from(User user, String redirectUrl, boolean authenticated, boolean newUser) {
        return new AuthResponse(
                user.getUserId() == null ? null : user.getUserId().toString(),
                user.getEmail(), user.getFirstName(), user.getLastName(), user.getProfileUrl(),
                redirectUrl, authenticated, newUser
        );
    }
}
