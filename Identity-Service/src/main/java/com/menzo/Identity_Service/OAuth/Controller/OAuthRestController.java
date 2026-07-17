package com.menzo.Identity_Service.OAuth.Controller;

import java.io.IOException;

import com.menzo.Identity_Service.OAuth.Service.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class OAuthRestController {

    @Autowired
    private OAuthService oAuthService;



    /*
    *
    *   Google OAuth - redirect URL
    *   Google redirects to this API, on user authorization,
    *   with authorization code
    *
    */
    @GetMapping("/grantcode")
    public void grantCode(@RequestParam("code") String code,
//                          @RequestParam("scope") String scope,
//                          @RequestParam("authuser") String authUser,
//                          @RequestParam("prompt") String prompt,
                          HttpServletResponse response) throws IOException {
        Cookie cookie = oAuthService.processGrantCode(code);
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:8080/index");
    }

}
