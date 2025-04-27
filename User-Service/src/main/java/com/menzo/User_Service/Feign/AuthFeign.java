package com.menzo.User_Service.Feign;

import com.menzo.User_Service.Dto.PasswordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("IDENTITY-SERVICE")
public interface AuthFeign {

    @PostMapping("/auth/encode-pwd")
    public String encodePassword(@RequestBody PasswordDto userPassword);
}
