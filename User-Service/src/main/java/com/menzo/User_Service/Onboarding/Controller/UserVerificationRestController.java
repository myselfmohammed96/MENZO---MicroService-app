package com.menzo.User_Service.Onboarding.Controller;

import com.menzo.User_Service.Onboarding.Dto.ResendOtpRequest;
//import com.menzo.User_Service.Global.Dto.Response.*;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResult;
import com.menzo.User_Service.Onboarding.Dto.Response.ResendOtpResponse;
import com.menzo.User_Service.Onboarding.Dto.VerifyOtpRequest;
import com.menzo.User_Service.Onboarding.Service.UserVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserVerificationRestController {

    private final UserVerificationService verificationService;

    @PostMapping(value = "/verify-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthResult result = verificationService.verifyOtp(request);
        return result.authenticated()
                ? AuthResponses.ok(result, "REGISTRATION_COMPLETED", "Email verified. Welcome aboard!")
                : AuthResponses.ok(result, "EMAIL_VERIFIED_LOGIN_REQUIRED",
                "Email verified and account created. Please sign in to continue.");
    }

    @PostMapping(value = "/resend-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ResendOtpResponse>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        return ResponseEntity.ok(ApiResponse.success("OTP_SENT",
                "A new verification code has been sent to your email.", verificationService.resendOtp(request)));
    }

}



//    @PostMapping(value = "/verify-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
//        return ResponseEntity.ok(ApiResponse.success("OTP_VERIFIED",
//                "Email verified successfully.", verificationService.verifyOtp(request)));
//    }