package com.menzo.User_Service.User.Address.Controller;

import com.menzo.User_Service.User.Address.Dto.UserAddressDto;
import com.menzo.User_Service.User.Address.Service.UserAddressQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserAddressController {

    @Autowired
    private UserAddressQueryService addressQueryService;



    /*
     *
     *  Get User Addresses
     *  User identified by user email
     *
     */
    @GetMapping("/address")
    public String getUserAddressByEmail(@RequestHeader("loggedInUser") String userEmail,
                                        Model model) {
        List<UserAddressDto> userAddresses = addressQueryService.getAllAddressByEmail(userEmail);
        model.addAttribute("userAddresses", userAddresses);
        return "Users/user-address";
    }
}
