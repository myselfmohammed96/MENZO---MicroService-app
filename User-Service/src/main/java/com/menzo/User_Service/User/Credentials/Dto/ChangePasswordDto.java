package com.menzo.User_Service.User.Credentials.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
