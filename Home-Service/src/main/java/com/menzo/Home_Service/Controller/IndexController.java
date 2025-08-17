package com.menzo.Home_Service.Controller;

import com.menzo.Home_Service.Dto.ClientSideUserDetailsDto;
import com.menzo.Home_Service.Service.UserService;
import com.menzo.Home_Service.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping("/index")
//    public String indexPage(@RequestHeader(value = "loggedInUser", required = false) String userEmail, Model model){
//        if (userEmail != null) {
//            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
//            model.addAttribute("user", user);
//        } else {
//            model.addAttribute("user", null);
//        }
//        return "IndexTemplates/index-page.html";
//    }

    @GetMapping("/index")
    public String indexPage(@CookieValue(value = "JWT", required = false) String token, Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            System.out.println("Vanthudichi... " + userEmail);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            user.display(); // *** check ***
            model.addAttribute("user", user);
        } else {
            System.out.println("Varala....**");
            model.addAttribute("user", null);
        }
        return "IndexTemplates/index-page.html";
    }

    @GetMapping("/all-categories")
    public String allCategories(Model model) {
        model.addAttribute("user", null);
        return "IndexTemplates/all-categories";
    }

}
