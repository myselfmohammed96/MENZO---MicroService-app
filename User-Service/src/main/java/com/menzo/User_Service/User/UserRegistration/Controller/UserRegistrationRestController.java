package com.menzo.User_Service.User.UserRegistration.Controller;

import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.UserRegistration.Dto.RegNewUser;
import com.menzo.User_Service.User.UserProfile.Dto.UserDto;
import com.menzo.User_Service.User.UserRegistration.Service.UserRegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRegistrationRestController {

    @Autowired
    private UserRegistrationService userRegistrationService;



    /*
     *
     *   Register new user (Customer)
     *   Form registration
     *
     */
    @PostMapping("user-signin")
    public ResponseEntity<?> createNewUser(@RequestBody RegNewUser newUser,
                                           HttpServletResponse response){
        Cookie jwtCookie = userRegistrationService.registerNewUser(newUser);
        response.addCookie(jwtCookie);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully.");
    }



    /*
    *
    *   Get Google OAuth user details
    *   For the user data got from the Google server
    *
    */
    @PostMapping("/google-oauth-access")
    public ResponseEntity<UserDto> googleOAuthUser(@RequestBody OAuthUserDto googleUser) {
        UserDto user = userRegistrationService.saveGoogleOAuthUser(googleUser);
        return ResponseEntity.ok(user);
    }
}
