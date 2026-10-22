package com.menzo.User_Service.Global.FeignClient;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
import com.menzo.User_Service.Global.FeignClient.Dto.*;
//import com.menzo.User_Service.User.UserRegistration.FeignClient.Dto.*;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "IDENTITY-SERVICE",
        path = "/internal/v1/auth",
        configuration = IdentityFeignConfig.class
)
public interface IdentityClient {

    @PostMapping("/token")
    ResponseEntity<ApiResponse<IdentityAuthData>> issueToken(@RequestBody IssueTokenRequest request);

    @PostMapping("/login")
    ResponseEntity<ApiResponse<IdentityAuthData>> login(@RequestBody IdentityLoginRequest request);

    @PostMapping("/google/verify")
    ApiResponse<GoogleProfile> verifyGoogleToken(@RequestBody GoogleVerifyRequest request);


    //
//    /*
//    *
//    *   Encode User password
//    *
//    */
//    @PostMapping("/auth/encode-pwd")
//    ResponseEntity<HashDto> hashPassword(@RequestBody HashDto userPassword);
//
//
//    /*
//     *
//     *  Verify user password hash
//     *
//     */
//    @PostMapping("/auth/verify-pwd")
//    ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyHashDto passwords);
//
////    @PostMapping("/auth/verify-password")
////    public Boolean verifyPassword(@RequestBody VerifyPasswordDto passwordDto);
//
//
//    /*
//    *
//    *   Generate JWT Token for given Customer email
//    *
//    */
////    @PostMapping("/auth/get-token")
////    public TokenMinimalDto generateToken(@RequestBody EmailDto userEmail);
//
//

}
