package com.menzo.User_Service.Feign;

import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Credentials.Dto.PasswordDto;
import com.menzo.User_Service.User.UserRegistration.Dto.TokenMinimalDto;
import com.menzo.User_Service.User.Credentials.Dto.VerifyPasswordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("IDENTITY-SERVICE")
public interface AuthFeign {



    /*
    *
    *   Encode User password
    *
    */
    @PostMapping("/auth/encode-pwd")
    public PasswordDto encodePassword(@RequestBody PasswordDto userPassword);



    /*
    *
    *   Generate JWT Token for given Customer email
    *
    */
    @PostMapping("/auth/get-token")
    public TokenMinimalDto generateToken(@RequestBody EmailDto userEmail);



    /*
    *
    *
    *
    */
    @PostMapping("/auth/verify-password")
    public Boolean verifyPassword(@RequestBody VerifyPasswordDto passwordDto);

}
