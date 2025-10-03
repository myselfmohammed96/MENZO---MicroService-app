package com.menzo.User_Service.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Gender;
import com.menzo.User_Service.Enums.Roles;

import java.time.LocalDate;

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
    private Roles roles;
    private String profileUrl;
    private boolean isActive;

    public UserDto() {}

    public UserDto(Long id, String firstName, String lastName,
                   String email, String phoneNumber, String password,
                   Roles roles, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.isActive = isActive;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.isActive = user.isActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return "\nUserDto:\nid: " + id + "\nfirstName: " + firstName + "\nlastName: " + lastName +
                "\nemail: " + email + "\nphoneNumber: " + phoneNumber + "\ngender: " + gender +
                "\nroles: " + roles;
    }
}
