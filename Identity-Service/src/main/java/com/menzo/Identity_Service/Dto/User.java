package com.menzo.Identity_Service.Dto;

import com.menzo.Identity_Service.Enum.Gender;
import com.menzo.Identity_Service.Enum.Roles;

import java.util.Date;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Roles roles;
    private boolean isActive;
    ///////////////////////////////////

    public User() {}

    public User(Long id, String firstName, String lastName, String email,
                String phoneNumber, String password, Roles roles, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.isActive = isActive;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void display() {
        System.out.println("id: " + id + "\nfirstName: " + firstName + "\nlastName: " + lastName +
                "\nemail: " + email + "\nphoneNumber: " + phoneNumber + "\nroles: " + roles +
                "\nisActive: " + isActive);
    }
}

