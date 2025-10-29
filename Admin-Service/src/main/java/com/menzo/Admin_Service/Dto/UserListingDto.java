package com.menzo.Admin_Service.Dto;

import com.menzo.Admin_Service.Enum.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListingDto {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private ActiveStatus activeStatus;

}
