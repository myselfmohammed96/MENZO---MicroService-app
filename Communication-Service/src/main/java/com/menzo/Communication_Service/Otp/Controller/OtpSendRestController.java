package com.menzo.Communication_Service.Otp.Controller;

import com.menzo.Communication_Service.Email.Dto.EmailDto;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Otp.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/send")
public class OtpSendRestController {

    @Autowired
    private OtpService otpService;


    /*
     *
     *   Send new OTP on user sign-in
     *
     */
    @PostMapping("sign-in-otp")
    public ResponseEntity<?> sendUserSignInOtp(@RequestBody EmailDto userEmail) {

        boolean otpSent = otpService.sendNewOtp(
                userEmail.getEmail(),
                Purpose.USER_SIGN_IN
        );

        //  response building
        if (otpSent) {
            return ResponseEntity.ok(Map.of("otpSent", true));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    /*
     *
     *   Resend OTP on user sign-in
     *
     */
    @PostMapping("/resend/sign-in-otp")
    public ResponseEntity<?> resendUserSignInOtp(@RequestBody EmailDto userEmail) {

        boolean otpSent = otpService.resendOtp(
                userEmail.getEmail(),
                Purpose.USER_SIGN_IN
        );

        //  response building
        if (otpSent) {
            return ResponseEntity.ok(Map.of("otpSent", true));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
