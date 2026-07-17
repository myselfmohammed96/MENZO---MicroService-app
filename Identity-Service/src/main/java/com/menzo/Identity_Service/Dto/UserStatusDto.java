package com.menzo.Identity_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusDto {

    private Long id;

    private String email;

    private boolean isActive;

    ////////////////////////////////

    public String toString() {
        return "UserStatusDto:\nid: " + id +
                "\nemail: " + email +
                "\nisActive: " + isActive + "\n";
    }
}
