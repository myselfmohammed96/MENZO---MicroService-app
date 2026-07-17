package com.menzo.Identity_Service.Dto;

import com.menzo.Identity_Service.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegNewUser {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String email;

    private Gender gender;

    private String password;

    private String confirmPassword;

    private String profilePic;

    /////////////////////////////////

    public String toString() {
        return "RegNewUser:\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nphoneNumber: " + phoneNumber +
                "\ndateOfBirth: " + dateOfBirth +
                "\nemail: " + email +
                "\ngender: " + gender + "\n";
    }
}
