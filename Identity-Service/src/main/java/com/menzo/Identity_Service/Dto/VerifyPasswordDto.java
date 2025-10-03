package com.menzo.Identity_Service.Dto;

public class VerifyPasswordDto {

    private String currentPassword;
    private String passwordInDB;

    public VerifyPasswordDto() {}

    public VerifyPasswordDto(String currentPassword, String passwordInDB) {
        this.currentPassword = currentPassword;
        this.passwordInDB = passwordInDB;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPasswordInDB() {
        return passwordInDB;
    }

    public void setPasswordInDB(String passwordInDB) {
        this.passwordInDB = passwordInDB;
    }

}
