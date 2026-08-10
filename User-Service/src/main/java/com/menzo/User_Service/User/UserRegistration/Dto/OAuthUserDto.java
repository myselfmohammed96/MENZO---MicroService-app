package com.menzo.User_Service.User.UserRegistration.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUserDto {

    private UUID userId;

    private String userName;

    private String email;

    private String profileUrl;

    private boolean isActive;

    /////////////////////////////////

    public String toString() {
        return "OAuthUserDto:\nid: " + userId +
                "\nuserName: " + userName +
                "\nemail: " + email +
                "\nprofileUrl: " + profileUrl +
                "\nisActive: " + isActive + "\n";
    }
}

