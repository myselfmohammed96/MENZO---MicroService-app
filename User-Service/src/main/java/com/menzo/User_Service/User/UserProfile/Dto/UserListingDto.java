package com.menzo.User_Service.User.UserProfile.Dto;

import com.menzo.User_Service.User.UserProfile.Enum.ActiveStatus;
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

    //////////////////////////////////////

    public UserListingDto(Long id,
                          String firstName,
                          String lastName,
                          String email,
                          String phoneNumber,
                          ActiveStatus activeStatus) {
        this.id = id;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.activeStatus = activeStatus;
    }

    public String toString() {
        return "UserListingDto:\nid: " + id +
                "\nfullName: " + fullName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\nactiveStatus: " + activeStatus + "\n";
    }
}
