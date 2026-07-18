package com.menzo.User_Service.User.Profile.Service;

import jakarta.servlet.http.Cookie;

public class UserUtilityService {


    /*
    *
    *   Create JWT cookie
    *
    */
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }
}
