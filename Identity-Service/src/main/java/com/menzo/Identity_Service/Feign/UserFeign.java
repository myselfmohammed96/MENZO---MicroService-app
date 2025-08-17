package com.menzo.Identity_Service.Feign;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Dto.UserStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE")
public interface UserFeign {

    @PostMapping("/user/get-by-email")
    public User getUserbyUserEmail(@RequestBody EmailDto userEmail);

    @GetMapping("/user/get-by-id")
    public UserStatusDto getUserByUserId(@RequestParam("id") Long userId);

//    @PostMapping("/user/get-cred-by-email")
//    public User getUserCredentialsbyUserEmail(@RequestBody EmailDto userEmail);

}
