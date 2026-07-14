package com.menzo.User_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Dto.OAuthUserDto;
import com.menzo.User_Service.Enums.Gender;
import com.menzo.User_Service.Enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles userType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "profile_pic")
    private String profileUrl;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ///////////////////////////////////////////////

    public User(OAuthUserDto user) {
        this.firstName = user.getUserName();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
    }

    public User(String firstName, String lastName, String phoneNumber,
         String email, LocalDate dateOfBirth, Gender gender,
         String userPassword){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.password = userPassword;
    }

    public String toString() {
        return "User:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ndateOfBirth: " + dateOfBirth +
                "\ngender: " + gender +
                "\nroles: " + userType +
                "\nisActive: " + isActive + "\n";
    }
}
