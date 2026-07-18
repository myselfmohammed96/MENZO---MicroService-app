package com.menzo.User_Service.User.Profile.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockUserDto {

    private Long userId;

    private boolean block;

    //////////////////////////////

    public String toString() {
        return "UpdateUserActiveStatusDto:\nuserId: " + userId +
                "\nblock: " + block + "\n";
    }
}
