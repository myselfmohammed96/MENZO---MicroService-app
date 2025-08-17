package com.menzo.Identity_Service.Dto;

public class UserStatusDto {

    private Long id;
    private String email;
    private boolean isActive;

    public UserStatusDto() {}

    public UserStatusDto(Long id, String email, boolean isActive) {
        this.id = id;
        this.email = email;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void display() {
        System.out.println("UserStatusDto:\nid: " + id + "\nemail: " + email +
                "\nisActive: " + isActive);
    }
}
