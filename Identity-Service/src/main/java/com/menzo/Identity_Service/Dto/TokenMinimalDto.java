package com.menzo.Identity_Service.Dto;

public class TokenMinimalDto {

    private String token;

    public TokenMinimalDto(){}

    public TokenMinimalDto(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
