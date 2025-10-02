package com.menzo.Identity_Service.Controller;

import com.menzo.Identity_Service.Dto.*;
import com.menzo.Identity_Service.Service.AuthService;
import com.menzo.Identity_Service.Service.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OAuthService oAuthService;

    //    To encode user password - for new user registration
    @PostMapping("/encode-pwd")
    public ResponseEntity<PasswordDto> encodePassword(@RequestBody PasswordDto userPassword){
        PasswordDto encodedPassword = authService.encryptPassword(userPassword);
        return ResponseEntity.ok(encodedPassword);
    }

    @GetMapping("get-by-token")
    public ResponseEntity<TokenDto> getByToken(@RequestParam String token){
        TokenDto tokenDto = authService.getByToken(token);
        return ResponseEntity.ok(tokenDto);
    }

    //  Form login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginCredentials loginCred, HttpServletResponse response) {
        Cookie cookie = authService.loginUser(loginCred);
        response.addCookie(cookie);
        return ResponseEntity.ok("Authentication successful");
    }

    //  OAuth redirect url
    @GetMapping("/grantcode")
    public void grantCode(@RequestParam("code") String code,
//                            @RequestParam("scope") String scope,
//                            @RequestParam("authuser") String authUser,
//                            @RequestParam("prompt") String prompt) { - throws IOException
                          HttpServletResponse response) throws IOException {
        Cookie cookie = oAuthService.processGrantCode(code);
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:8080/index");
    }

    @PostMapping("/get-token")
    public ResponseEntity<TokenMinimalDto> getToken(@RequestBody EmailDto userEmail) {
        System.out.println(userEmail.getEmail());
        if(userEmail == null || userEmail.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User email empty");
        }

        String token = authService.generateToken(userEmail.getEmail());
//        Cookie jwtCookie = authService.createCookie(token);

        return ResponseEntity.ok(new TokenMinimalDto(token));
    }
}
