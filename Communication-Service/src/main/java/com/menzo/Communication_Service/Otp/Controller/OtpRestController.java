package com.menzo.Communication_Service.Otp.Controller;

import com.menzo.Communication_Service.Global.Dto.*;
import com.menzo.Communication_Service.Otp.Service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/otp")
@RequiredArgsConstructor
public class OtpRestController {

    private final OtpService otpService;


    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SendOtpResponse>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        SendOtpResponse data = otpService.sendOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP_SENT", "OTP sent successfully.", data));
    }

    @PostMapping(value = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        VerifyOtpResponse data = otpService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP_VERIFIED", "OTP verified successfully.", data));
    }







//    ------------------------------------------



    /*
     *
     *   Resend OTP on user sign-in
     *
     */
//    @PostMapping("/resend/sign-in-otp")
//    public ResponseEntity<?> resendUserSignInOtp(@RequestBody EmailDto userEmail) {
//
//        boolean otpSent = otpService.resendOtp(
//                userEmail.getEmail(),
//                Purpose.USER_SIGN_IN
//        );
//
//        //  response building
//        if (otpSent) {
//            return ResponseEntity.ok(Map.of("otpSent", true));
//        } else {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }

}
