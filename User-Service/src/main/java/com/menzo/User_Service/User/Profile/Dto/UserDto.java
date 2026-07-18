package com.menzo.User_Service.User.Profile.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Enum.Gender;
import com.menzo.User_Service.User.Profile.Enum.UserTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    @JsonFormat(pattern = "dd-MM-yyy")
    private LocalDate dateOfBirth;

    private Gender gender;

    private String password;

    private UserTypes userTypes;

    private String profileUrl;

    private boolean isActive;

    ///////////////////////////////////////////

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.userTypes = user.getUserType();
        this.isActive = user.isActive();
    }

    public String toString() {
        return "\nUserDto:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ngender: " + gender +
                "\nuserType: " + userTypes + "\n";
    }
}
