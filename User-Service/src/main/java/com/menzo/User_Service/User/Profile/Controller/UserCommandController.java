package com.menzo.User_Service.User.Profile.Controller;

import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Profile.Dto.ClientSideUserDetailsDto;
import com.menzo.User_Service.User.Profile.Dto.UserDetailsDto;
import com.menzo.User_Service.User.Profile.Service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserCommandController {

    @Autowired
    private UserQueryService userQueryService;



    /*
    *
    *   Get user profile
    *   User identified by user email
    *
    */
    @GetMapping("/profile")
    public String getUserProfileByEmail(@RequestHeader("loggedInUser") String userEmail,
                                        Model model) {
        ClientSideUserDetailsDto user = userQueryService.getUserDetailsForClientSide(new EmailDto(userEmail));
        UserDetailsDto userDetails = userQueryService.getUserDetailsByEmail(userEmail);

        model.addAttribute("user", user);
        model.addAttribute("userDetails", userDetails);
        return "Users/user-profile";
    }
}
