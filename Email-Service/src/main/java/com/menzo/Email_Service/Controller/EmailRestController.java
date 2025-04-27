package com.menzo.Email_Service.Controller;

import com.menzo.Email_Service.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("otp-verification")
    public void otpVerification(@RequestParam String userEmail){
        emailService.otpVerification(userEmail);
    }
}
