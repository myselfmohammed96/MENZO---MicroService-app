package com.menzo.User_Service.User.UserRegistration.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.UserProfile.Enum.Gender;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must be at most 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must be at most 50 characters.")
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^(?:[6-9]\\d{9}|(?:\\+91|91|0)[6-9]\\d{9})$",
            message = "Invalid phone number."
    )
    private String phoneNumber;

    @NotNull(message = "Date of birth is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email address.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    private String email;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    private String profilePic;

    /////////////////////////////////////////////////

    public String toString() {
        return "RegNewUser:\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ngender: " + gender +
                "\ndateOfBirth: " + dateOfBirth + "\n";
    }
}
