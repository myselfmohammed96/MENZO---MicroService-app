package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.RegNewUser;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("user-signin")
    public void createNewUser(@ModelAttribute RegNewUser newUser){
        userService.saveNewUser(newUser);
    }

//    @GetMapping("health-check")
//    public String healthCheck(){
//        return "hello";
//    }

}
