package com.menzo.Identity_Service.Dto;

public class TokenDto {

    private Long tokenId;
    private String token;
    private boolean loggedOut;
    private Long userId;
    private boolean userIsActive;

    public TokenDto() {}

    public TokenDto(Long tokenId, String token, boolean loggedOut,
                    Long userId, boolean userIsActive) {
        this.tokenId = tokenId;
        this.token = token;
        this.loggedOut = loggedOut;
        this.userId = userId;
        this.userIsActive = userIsActive;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(boolean userIsActive) {
        this.userIsActive = userIsActive;
    }
}
