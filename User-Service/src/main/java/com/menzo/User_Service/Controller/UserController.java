package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.ClientSideUserDetailsDto;
import com.menzo.User_Service.Dto.EmailDto;
import com.menzo.User_Service.Dto.UserDetailsDto;
import com.menzo.User_Service.Dto.UserDto;
import com.menzo.User_Service.Service.UserRetrievalService;
import com.menzo.User_Service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRetrievalService userRetrievalService;



    //    -------------------------

    @GetMapping("/profile")
    public String getUserProfileByEmail(@RequestHeader("loggedInUser") String userEmail, Model model) {
        ClientSideUserDetailsDto user = userRetrievalService.getUserDetailsForClientSide(new EmailDto(userEmail));
        UserDetailsDto userDetails = userRetrievalService.getUserDetailsByEmail(userEmail);

        model.addAttribute("user", user);
        model.addAttribute("userDetails", userDetails);
        return "Users/user-profile";
    }

}
