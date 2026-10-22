package com.menzo.User_Service.Onboarding.Dto;

import com.menzo.User_Service.User.Profile.Enum.Gender;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = {"password", "confirmPassword", "profilePic"})
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
    @Past(message = "Date of birth must be in the past.")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
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

    @Size(max = 2048, message = "Redirect URL is too long.")
    private String redirectUrl;

    // Optional
    private MultipartFile profilePic;
}
//----------------------------------
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class RegNewUser {
//
//    @NotBlank(message = "First name is required.")
//    @Size(max = 50, message = "First name must be at most 50 characters.")
//    private String firstName;
//
//    @NotBlank(message = "Last name is required.")
//    @Size(max = 50, message = "Last name must be at most 50 characters.")
//    private String lastName;
//
//    @NotBlank(message = "Phone number is required.")
//    @Pattern(
//            regexp = "^(?:[6-9]\\d{9}|(?:\\+91|91|0)[6-9]\\d{9})$",
//            message = "Invalid phone number."
//    )
//    private String phoneNumber;
//
//    @NotNull(message = "Date of birth is required.")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    private LocalDate dateOfBirth;
//
//    @NotBlank(message = "Email is required.")
//    @Email(message = "Invalid email address.")
//    @Size(max = 100, message = "Email must not exceed 100 characters.")
//    private String email;
//
//    @NotNull(message = "Gender is required.")
//    private Gender gender;
//
//    @NotBlank(message = "Password is required.")
//    @Size(min = 8, max = 100, message = "Password must be 8-100 characters.")
//    private String password;
//
//    @NotBlank(message = "Confirm password is required.")
//    private String confirmPassword;
//
//    private String redirectUrl;
//
//    private String profilePic;
//
//    /////////////////////////////////////////////////
//
//    public String toString() {
//        return "RegNewUser:\nfirstName: " + firstName +
//                "\nlastName: " + lastName +
//                "\nemail: " + email +
//                "\nphoneNumber: " + phoneNumber +
//                "\ngender: " + gender +
//                "\ndateOfBirth: " + dateOfBirth + "\n";
//    }
//}
