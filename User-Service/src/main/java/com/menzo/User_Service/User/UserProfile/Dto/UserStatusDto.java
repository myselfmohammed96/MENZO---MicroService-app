package com.menzo.User_Service.User.UserProfile.Dto;

import com.menzo.User_Service.User.UserProfile.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusDto {

    private Long userId;

    private String email;

    private boolean isActive;

    ////////////////////////////////

    public UserStatusDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.isActive = user.isActive();
    }

    public String toString() {
        return "UserStatusDto:\nuserId: " + userId +
                "\nemail: " + email +
                "\nisActive: " + isActive + "\n";
    }
}
