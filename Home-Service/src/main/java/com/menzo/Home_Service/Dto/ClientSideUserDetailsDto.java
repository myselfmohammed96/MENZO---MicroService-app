package com.menzo.Home_Service.Dto;

import com.menzo.Home_Service.Enum.Roles;

public class ClientSideUserDetailsDto {

    private String firstName;
    private String lastName;
    private Roles roles;

    public ClientSideUserDetailsDto() {}

    public ClientSideUserDetailsDto(String firstName, String lastName, Roles roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public void display() {
        System.out.println("firstName: " + firstName + "\nlastName: " + lastName +
                "\nroles: " + roles);
    }
}
