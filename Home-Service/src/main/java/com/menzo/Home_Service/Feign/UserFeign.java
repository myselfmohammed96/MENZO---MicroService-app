package com.menzo.Home_Service.Feign;

import com.menzo.Home_Service.Dto.ClientSideUserDetailsDto;
import com.menzo.Home_Service.Dto.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserFeign {

    @PostMapping("/user/get-user-for-client-side")
    public ClientSideUserDetailsDto getUserDetailsForClientSide(@RequestBody EmailDto userEmail);
}
