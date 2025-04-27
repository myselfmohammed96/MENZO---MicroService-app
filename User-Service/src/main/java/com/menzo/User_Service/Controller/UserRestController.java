package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.EmailDto;
import com.menzo.User_Service.Dto.RegNewUser;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Feign.EmailFeign;
import com.menzo.User_Service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @GetMapping("health-check")
    public String healthCheck(){
        return "hello";
    }

//    @PostMapping("user-signin")
//    public void createNewUser(@RequestBody RegNewUser newUser){
//        userService.saveNewUser(newUser);
//    }

    @PostMapping("/get-by-email")
    public User getUserbyUserEmail(@RequestBody EmailDto userEmail){
        return userService.getUserbyEmail(userEmail);
//        return null;
    }

}
