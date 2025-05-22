package com.menzo.API_Gateway.Dto;

public class TokenDto {

    private Long tokenId;
    private String token;
    private boolean loggedOut;
    private Long userId;

    public TokenDto(){}

    public TokenDto(String token, boolean loggedOut, Long userId){
        this.token = token;
        this.loggedOut = loggedOut;
        this.userId = userId;
    }

    public TokenDto(Long tokenId, String token, boolean loggedOut, Long userId){
        this.tokenId = tokenId;
        this.token = token;
        this.loggedOut = loggedOut;
        this.userId = userId;
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
}
