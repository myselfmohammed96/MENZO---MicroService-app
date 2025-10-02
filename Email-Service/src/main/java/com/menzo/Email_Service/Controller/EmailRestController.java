package com.menzo.Email_Service.Controller;

import com.menzo.Email_Service.Dto.EmailDto;
import com.menzo.Email_Service.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody EmailDto emailDto) {
//        emailService.verifyOtp(emailDto);
        return ResponseEntity.ok("Otp sent successfully!");
    }

    @GetMapping
    public void sendIt() {
        emailService.sendOtp();
    }

}
