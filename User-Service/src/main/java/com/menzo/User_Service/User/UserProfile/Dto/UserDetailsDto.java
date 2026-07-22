package com.menzo.User_Service.User.UserProfile.Dto;

import com.menzo.User_Service.User.UserProfile.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private boolean passwordPresent;

    private LocalDateTime createdAt;

    private boolean isActive;

    ////////////////////////////////////////

    public String toString() {
        return "User:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ndateOfBirth: " + dateOfBirth +
                "\ngender: " + gender +
                "\ncreatedAt: " + createdAt +
                "\nisActive: " + isActive + "\n";
    }
}
