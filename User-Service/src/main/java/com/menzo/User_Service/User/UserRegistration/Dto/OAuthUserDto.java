package com.menzo.User_Service.User.Dto;

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

    public OAuthUserDto(String email,
                        String userName,
                        String profileUrl) {
        this.email = email;
        this.userName = userName;
        this.profileUrl = profileUrl;
    }

    public String toString() {
        return "OAuthUserDto:\nid: " + id +
                "\nuserName: " + userName +
                "\nemail: " + email +
                "\nprofileUrl: " + profileUrl +
                "\nisActive: " + isActive + "\n";
    }
}

