package com.menzo.User_Service.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private String email;

    ///////////////////////////

    public String toString() {
        return "EmailDto:\nemail: " + email;
    }

}
