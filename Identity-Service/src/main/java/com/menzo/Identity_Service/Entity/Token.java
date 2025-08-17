package com.menzo.Identity_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "is_logged_out")
    private boolean loggedOut;

    @Column(name = "user_id")
    private Long userId;

    public Token(){}

    public Token(String token, boolean loggedOut, Long userId){
        this.token = token;
        this.loggedOut = loggedOut;
        this.userId = userId;
    }

    public Token(Long tokenId, String token, boolean loggedOut, Long userId) {
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
