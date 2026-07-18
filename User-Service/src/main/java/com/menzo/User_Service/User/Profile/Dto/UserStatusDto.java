package com.menzo.User_Service.User.Profile.Dto;

import com.menzo.User_Service.User.Profile.Entity.User;
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
        this.userId = user.getId();
        this.email = user.getEmail();
        this.isActive = user.isActive();
    }

    public String toString() {
        return "UserStatusDto:\nuserId: " + userId +
                "\nemail: " + email +
                "\nisActive: " + isActive + "\n";
    }
}
