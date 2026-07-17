package com.menzo.Identity_Service.Authentication.Controller;

import com.menzo.Identity_Service.Authentication.Service.AuthService;
import com.menzo.Identity_Service.Authentication.Dto.LoginCredentials;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthService authService;



    /*
     *
     *
     *
     */
    //  Form login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginCredentials loginCred,
                                       HttpServletResponse response) {
        Cookie cookie = authService.loginUser(loginCred);
        response.addCookie(cookie);
        return ResponseEntity.ok("Authentication successful");
    }
}
