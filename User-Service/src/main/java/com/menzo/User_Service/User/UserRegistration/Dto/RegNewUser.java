package com.menzo.User_Service.User.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegNewUser {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Gender gender;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String password;

    private String confirmPassword;

    private String profilePic;

    /////////////////////////////////////////////////

    public String toStringy() {
        return "RegNewUser:\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ngender: " + gender +
                "\ndateOfBirth: " + dateOfBirth + "\n";
    }
}
