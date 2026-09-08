package com.menzo.Communication_Service.Global.Feign;

import com.menzo.Communication_Service.Email.Dto.HashDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "IDENTITY-SERVICE", url = "http://localhost:9090")
public interface AuthFeign {


    /*
     *
     *   Hash OTP
     *
     */
    @PostMapping("/auth/hash-otp")
    public ResponseEntity<HashDto> hashOtp(@RequestBody HashDto otp);

}
