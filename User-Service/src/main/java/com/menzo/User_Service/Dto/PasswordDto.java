package com.menzo.User_Service.Dto;

public class PasswordDto {

    private String password;

    public PasswordDto(){}

    public PasswordDto(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "PasswordDto { password = '" + password + "' }";
    }
}
