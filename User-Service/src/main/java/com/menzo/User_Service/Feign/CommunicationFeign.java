package com.menzo.User_Service.Feign;

import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("COMMUNICATION-SERVICE")
public interface CommunicationFeign {

    /*
     *
     *   Send OTP to user email
     *
     */
    @PostMapping("")
    ResponseEntity<Boolean> sendUserOtp(@RequestBody EmailDto userEmail);
}
