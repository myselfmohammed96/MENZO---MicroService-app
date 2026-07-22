package com.menzo.User_Service.User.UserProfile.Dto;

import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Enum.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientSideUserDetailsDto {

    private String firstName;

    private String lastName;

    private UserTypes userType;

    //////////////////////////////////

    public ClientSideUserDetailsDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
    }

    public String toString() {
        return "ClientSideUserDetailsDto:\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nroles: " + userType + "\n";
    }
}
