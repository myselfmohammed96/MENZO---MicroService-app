package com.menzo.User_Service.Dto;

public class UserMinimalDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public UserMinimalDto() {}

    public UserMinimalDto(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public UserMinimalDto(Long id, String firstName, String lastName,
                          String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "UserMinimalDto:\nid: " + id + "\nfirstName: " + firstName +
                "\nlastName: " + lastName + "\nphoneNumber: " + phoneNumber + "\n";
    }
}
