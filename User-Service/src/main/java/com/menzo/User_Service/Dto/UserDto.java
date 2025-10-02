package com.menzo.User_Service.Dto;

//import com.menzo.User_Service.Entity.OauthUser;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Roles;

public class UserDto {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Roles roles;
    private String profileUrl;
    private User mainUser;
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

//    public UserDto(OauthUser user) {
//        this.id = user.getId();
//        this.userName = user.getUserName();
//        this.email = user.getEmail();
//        this.profileUrl = user.getProfileUrl();
//        this.roles = user.getRoles();
//        this.mainUser = user.getMainUser();
//        this.isActive = isActive();
//    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fullName) {
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void display() {
        System.out.println("firstName: " + firstName + "\nlastName: " + lastName +
                "\nemail: " + email + "\nphoneNumber: " + phoneNumber + "\nroles: " + roles +
                "\nisActive: " + isActive);
    }
}
