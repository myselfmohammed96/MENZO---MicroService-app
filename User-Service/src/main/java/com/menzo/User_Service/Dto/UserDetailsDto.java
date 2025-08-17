package com.menzo.User_Service.Dto;

import com.menzo.User_Service.Enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDetailsDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private LocalDateTime createdAt;
    private boolean isActive;

    public UserDetailsDto() {}

    public UserDetailsDto(Long id, String firstName, String lastName, String email,
                          String phoneNumber, LocalDate dateOfBirth, Gender gender,
                          LocalDateTime createdAt, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.createdAt = createdAt;
        this.isActive = isActive;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void display() {
        System.out.println("User:\nid: " + id + "\nfirstName: " + firstName + "\nlastName: " + lastName +
                "\nemail: " + email + "\nphoneNumber: " + phoneNumber + "\ndateOfBirth: " + dateOfBirth +
                "\ngender: " + gender + "\ncreatedAt: " + createdAt + "\nisActive: " + isActive);
    }
}
