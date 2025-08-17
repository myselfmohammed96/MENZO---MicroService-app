//package com.menzo.User_Service.Dto;
//
//import com.menzo.User_Service.Entity.User;
//import com.menzo.User_Service.Enums.Roles;
//
//public class UserCredentialsDto {
//
//    private Long id;
//    private String email;
//    private String password;
//    private Roles roles;
//
//    public UserCredentialsDto() {}
//
//    public UserCredentialsDto(Long id, String email, String password) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//    }
//
//    public UserCredentialsDto(User user) {
//        this.id = user.getId();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//    }
//
//    public UserCredentialsDto(String email, String password, Roles roles) {
//        this.email = email;
//        this.password = password;
//        this.roles = roles;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Roles getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Roles roles) {
//        this.roles = roles;
//    }
//}
