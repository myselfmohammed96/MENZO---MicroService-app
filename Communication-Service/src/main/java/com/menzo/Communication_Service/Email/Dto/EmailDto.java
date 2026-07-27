package com.menzo.Communication_Service.Email.Dto;

public class EmailDto {

    private String email;

    public EmailDto() {}

    public EmailDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "EmailDto:\nemail: " + email + "\n";
    }
}
