//package com.menzo.Identity_Service.Controller;
//
//import com.menzo.Identity_Service.Dto.LoginCredentials;
//import com.menzo.Identity_Service.Service.AuthService;
//import com.menzo.Identity_Service.Service.OAuthService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    AuthService authService;
//
//    @Autowired
//    OAuthService oAuthService;
//
//    //    End point for submitting the login form
////    @PostMapping("/login")
////    public String loginUser(@ModelAttribute LoginCredentials loginCred, HttpServletResponse response) throws Exception{
////        System.out.println(loginCred.getEmail() + " #.# " + loginCred.getPassword());
////        Cookie cookie = authService.loginUser(loginCred);
////        response.addCookie(cookie);
////        String redirectUrl = UriComponentsBuilder
////                .fromUriString("http://localhost:8080")
////                .pathSegment("index")
////                .toUriString();
////        return "redirect:" + redirectUrl;
////    }
//
//
//
//
//
//
//}
