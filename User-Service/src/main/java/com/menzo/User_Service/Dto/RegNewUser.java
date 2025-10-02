package com.menzo.User_Service.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Enums.Gender;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RegNewUser {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String email;
    private Gender gender;
    private String password;
    private String confirmPassword;
    private String profilePic;

    public RegNewUser() {}

    public RegNewUser(String firstName, String lastName, String phoneNumber,
                      LocalDate dateOfBirth, String email, Gender gender,
                      String password, String confirmPassword, String profilePic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void display() {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.profilePic = profilePic;
        System.out.println("firstName: " + firstName + "\nlastName: " + lastName +
                "\nphoneNumber: " + phoneNumber + "\ndateOfBirth: " + dateOfBirth +
                "\nemail: " + email + "\ngender: " + gender);
    }
}
