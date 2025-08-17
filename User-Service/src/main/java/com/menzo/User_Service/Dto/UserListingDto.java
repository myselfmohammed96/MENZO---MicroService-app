package com.menzo.User_Service.Dto;

import com.menzo.User_Service.Enums.ActiveStatus;

public class UserListingDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private ActiveStatus activeStatus;

    public UserListingDto() {}

    public UserListingDto(Long id, String firstName, String lastName, String email,
                          String phoneNumber, ActiveStatus activeStatus) {
        this.id = id;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.activeStatus = activeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void display() {
        System.out.println("UserListingDto:\nid: " + id + "\nfullName: " + fullName +
                "\nemail: " + email + "\nphoneNumber: " + phoneNumber + "\nactiveStatus: " + activeStatus);
    }
}
