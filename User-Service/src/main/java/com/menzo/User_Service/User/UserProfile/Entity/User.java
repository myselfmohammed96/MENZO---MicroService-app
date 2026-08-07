package com.menzo.User_Service.User.UserProfile.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Customer.Entity.Customer;
import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.UserProfile.Enum.Gender;
import com.menzo.User_Service.User.UserProfile.Enum.UserTypes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Customer customer;

    ///////////////////////////////////////////////

    public User(OAuthUserDto googleUser) {
        this.firstName = googleUser.getUserName();
        this.email = googleUser.getEmail();
        this.profileUrl = googleUser.getProfileUrl();
    }

    public String toString() {
        return "User:\nuserId: " + userId +
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
