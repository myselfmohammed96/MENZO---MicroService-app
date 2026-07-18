package com.menzo.User_Service.User.Profile.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.Profile.Enum.Gender;
import com.menzo.User_Service.User.Profile.Enum.UserTypes;
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
    private UserTypes userType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "profile_pic")
    private String profileUrl;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ///////////////////////////////////////////////

    public User(OAuthUserDto googleUser) {
        this.firstName = googleUser.getUserName();
        this.email = googleUser.getEmail();
        this.profileUrl = googleUser.getProfileUrl();
    }

    public User(String firstName,
                String lastName,
                String email,
                String phoneNumber,
                Gender gender,
                UserTypes userType,
                LocalDate dateOfBirth,
                String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userType = userType;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public String toString() {
        return "User:\nid: " + id +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nemail: " + email +
                "\nphoneNumber: " + phoneNumber +
                "\ngender: " + gender +
                "\nuserType: " + userType +
                "\ndateOfBirth: " + dateOfBirth +
                "\nisActive: " + isActive + "\n";
    }
}
