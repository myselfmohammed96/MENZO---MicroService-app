package com.menzo.User_Service.Dto;

public class OAuthUserDto {

    private Long id;
    private String userName;
    private String email;
    private String profileUrl;
    private boolean isActive;
    /////////////////////////////////

    public OAuthUserDto() {}

    public OAuthUserDto(String email, String userName, String profileUrl) {
        this.email = email;
        this.userName = userName;
        this.profileUrl = profileUrl;
    }

    public OAuthUserDto(Long id, String userName, String email,
                        String profileUrl, boolean isActive) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.profileUrl = profileUrl;
        this.isActive = isActive;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return "OAuthUserDto:\nid: " + id + "\nuserName: " + userName + "\nemail: " + email +
                "\nprofileUrl: " + profileUrl + "\nisActive: " + isActive;
    }
}

