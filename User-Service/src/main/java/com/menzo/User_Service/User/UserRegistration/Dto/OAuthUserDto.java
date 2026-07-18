package com.menzo.User_Service.User.UserRegistration.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUserDto {

    private Long id;

    private String userName;

    private String email;

    private String profileUrl;

    private boolean isActive;

    /////////////////////////////////

    public String toString() {
        return "OAuthUserDto:\nid: " + id +
                "\nuserName: " + userName +
                "\nemail: " + email +
                "\nprofileUrl: " + profileUrl +
                "\nisActive: " + isActive + "\n";
    }
}

