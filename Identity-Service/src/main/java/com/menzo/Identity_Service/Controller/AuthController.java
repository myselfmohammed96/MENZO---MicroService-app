package com.menzo.Identity_Service.Controller;

import com.menzo.Identity_Service.Dto.LoginCredentials;
import com.menzo.Identity_Service.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

//    End point for submitting the login form

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginCredentials loginCred, HttpServletResponse response) throws Exception{
        Cookie cookie = authService.loginUser(loginCred);
        response.addCookie(cookie);
        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .toUriString();
        return "redirect:" + redirectUrl;
    }

}
