package com.menzo.Communication_Service.Global.Feign;

import com.menzo.Communication_Service.Email.Dto.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserFeign {


    /*
     *
     *   Get username by user email
     *
     */
    @PostMapping("/user/get-username")
    public ResponseEntity<?> getUsername(@RequestBody EmailDto userEmail);

}
