package com.menzo.User_Service.Dto;

import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Roles;

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

    public ClientSideUserDetailsDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles();
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
