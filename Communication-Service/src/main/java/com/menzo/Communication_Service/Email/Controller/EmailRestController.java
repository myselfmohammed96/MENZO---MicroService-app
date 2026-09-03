package com.menzo.Communication_Service.Email.Controller;

import com.menzo.Communication_Service.Email.Dto.EmailDto;
import com.menzo.Communication_Service.Email.Dto.EmailRequest;
import com.menzo.Communication_Service.Email.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/send")
    public void send(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException, UnsupportedEncodingException {
        emailService.send(emailRequest);
    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody EmailDto emailDto) {
////        emailService.verifyOtp(emailDto);
//        return ResponseEntity.ok("Otp sent successfully!");
//    }


//    @GetMapping
//    public void sendIt() {
//        emailService.sendOtp();
//    }

}
