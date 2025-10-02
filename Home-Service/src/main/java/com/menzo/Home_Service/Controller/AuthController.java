package com.menzo.Home_Service.Controller;

import com.menzo.Home_Service.Dto.LoginCredectials;
import com.menzo.Home_Service.Dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/sign-in")
    public String signInForm(Model model){
        model.addAttribute("user", new User());
        return "AuthTemplates/sign-in-form";
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("loginCred", new LoginCredectials());
        return "AuthTemplates/log-in-form";
    }

}
