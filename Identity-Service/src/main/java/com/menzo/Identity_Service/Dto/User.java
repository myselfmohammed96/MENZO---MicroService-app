package com.menzo.Identity_Service.Dto;

import com.menzo.Identity_Service.Enum.Gender;
import com.menzo.Identity_Service.Enum.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private Roles roles;

    private boolean isActive;

    ///////////////////////////////////

    public String toString() {
        return "User:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\nroles: " + roles +
                "\nisActive: " + isActive + "\n";
    }
}

