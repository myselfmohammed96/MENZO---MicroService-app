package com.menzo.User_Service.User.Profile.Credentials.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyHashDto {

    private String current;

    private String storedHash;

}
