package com.menzo.User_Service.User.UserProfile.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockUserDto {

    private UUID userId;

    private boolean block;

    //////////////////////////////

    public String toString() {
        return "UpdateUserActiveStatusDto:\nuserId: " + userId +
                "\nblock: " + block + "\n";
    }
}
