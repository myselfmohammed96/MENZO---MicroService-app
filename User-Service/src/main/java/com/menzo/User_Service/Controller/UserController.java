package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.RegNewUser;
import com.menzo.User_Service.Dto.UserDto;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("user-signin")
    public String createNewUser(@ModelAttribute RegNewUser newUser){
        UserDto savedUser = userService.saveNewUser(newUser);
        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .pathSegment("index")
                .toUriString();
        return "redirect:" + redirectUrl;
    }

}
