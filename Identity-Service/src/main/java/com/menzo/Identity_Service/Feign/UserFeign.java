package com.menzo.Identity_Service.Feign;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.Dto.RegNewUser;
import com.menzo.Identity_Service.Dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserFeign {

    @PostMapping("/user/user-signin")
    public void createNewUser(@RequestBody RegNewUser newUser);

    @PostMapping("/user/get-by-email")
    public User getUserbyUserEmail(@RequestBody EmailDto userEmail);

}
