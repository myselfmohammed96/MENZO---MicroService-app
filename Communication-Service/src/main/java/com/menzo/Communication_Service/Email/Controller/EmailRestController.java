package com.menzo.Communication_Service.Email.Controller;

import com.menzo.Communication_Service.Email.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;


    /*
     *
     *   Send OTP to user email
     *
     */
//    @PostMapping("/send-user-otp")
//    public ResponseEntity<Boolean> sendUserOtp(@RequestBody EmailDto userEmail) {
//        boolean otpSent = emailService.sendUserOtp(userEmail);
//        if (otpSent) {
//            return ResponseEntity.ok(Boolean.TRUE);
//        } else {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }












//    @PostMapping("/send")
//    public void send(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException, UnsupportedEncodingException {
//        emailService.send(emailRequest);
//    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody EmailDto emailDto) {

    /// /        emailService.verifyOtp(emailDto);
//        return ResponseEntity.ok("Otp sent successfully!");
//    }


//    @GetMapping
//    public void sendIt() {
//        emailService.sendOtp();
//    }

}
