package com.menzo.User_Service.Feign;

import com.menzo.User_Service.Dto.EmailDto;
import com.menzo.User_Service.Dto.PasswordDto;
import com.menzo.User_Service.Dto.TokenMinimalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("IDENTITY-SERVICE")
public interface AuthFeign {

    @PostMapping("/auth/encode-pwd")
    public PasswordDto encodePassword(@RequestBody PasswordDto userPassword);

    @PostMapping("/auth/get-token")
    public TokenMinimalDto getToken(@RequestBody EmailDto userEmail);
}
