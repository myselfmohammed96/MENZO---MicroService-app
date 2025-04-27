package com.menzo.API_Gateway.Dto;

public class TokenDto {

    private Integer tokenId;
    private String token;
    private boolean loggedOut;
    private Integer userId;

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
