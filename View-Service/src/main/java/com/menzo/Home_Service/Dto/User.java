package com.menzo.Home_Service.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Home_Service.Enum.Gender;
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
public class User {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String email;

    private Gender gender;

    private String password;

    private String confirmPassword;

    private String profilePic;

}
