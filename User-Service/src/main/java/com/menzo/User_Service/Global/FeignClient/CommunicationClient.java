package com.menzo.User_Service.Global.FeignClient;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
import com.menzo.User_Service.Global.FeignClient.Dto.OtpVerifyRequest;
import com.menzo.User_Service.Global.FeignClient.Dto.OtpVerifyResponse;
import com.menzo.User_Service.Global.FeignClient.Dto.SendOtpRequest;
import com.menzo.User_Service.Global.FeignClient.Dto.SendOtpResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "COMMUNICATION-SERVICE",
        path = "/internal/v1/otp",
        configuration = FeignClientConfig.class
)
public interface CommunicationClient {

    @PostMapping("/send")
    ApiResponse<SendOtpResponse> sendOtp(@RequestBody SendOtpRequest request);

    @PostMapping("/verify")
    ApiResponse<OtpVerifyResponse> verifyOtp(@RequestBody OtpVerifyRequest request);

}


//package com.menzo.User_Service.Global.Feign;
//
//import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient("COMMUNICATION-SERVICE")
//public interface CommunicationFeign {
//
//    /*
//     *
//     *   Send new OTP to user email on user sign-in
//     *
//     */
//    @PostMapping("/send/sign-in-otp ")
//    ResponseEntity<?> sendUserSignInOtp(@RequestBody EmailDto userEmail);
//
//}



//package com.menzo.User_Service.Feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "EMAIL-SERVICE")
//public interface EmailFeign {
//
//    @GetMapping("/email/otp-verification")
//    public void otpVerification(@RequestParam String userEmail);
//}


