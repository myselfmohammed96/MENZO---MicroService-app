package com.menzo.Identity_Service.Controller;

import com.menzo.Identity_Service.Dto.PasswordDto;
import com.menzo.Identity_Service.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    AuthService authService;

//    To encode user password - for new user registration

    @PostMapping("/encode-pwd")
    public String encodePassword(@RequestBody PasswordDto userPassword){
        System.out.println("Received PasswordDto object from controller: " + userPassword);
        System.out.println("Password from the controller: " + userPassword.getPassword());
        return authService.encryptPassword(userPassword);
    }

//    @GetMapping("/health-check")
//    public String healthCheck(){
//        return "Hello Maplaee..";
//    }
}
