package com.menzo.User_Service.User.UserRegistration.Controller;

import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.UserRegistration.Dto.RegNewUser;
import com.menzo.User_Service.User.UserProfile.Dto.UserDto;
import com.menzo.User_Service.User.UserRegistration.Service.UserRegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRegistrationRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);

    @Autowired
    private UserRegistrationService userRegistrationService;


    /*
     *
     *   Register new user (Customer)
     *   Form registration
     *
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegNewUser newUser,
                                           BindingResult result) {
//                                           HttpServletResponse response){

        //  new user details validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));

            logger.warn("Validation failed for new registered user: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        boolean otpSent = userRegistrationService.addNewUser(newUser);

        //  response building
        if (otpSent) {
            logger.info("OTP sent to user email '{}' for user sign-in.", newUser.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } else {
            logger.info("Cannot send OTP to user email '{}' for user sign-in.", newUser.getEmail());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        //  ## user enter otp
        //  ## if otp is valid - confirm reg user details as signed in
        //  then proceed further.

//        Cookie jwtCookie = userRegistrationService.registerNewUser(newUser);
//        response.addCookie(jwtCookie);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("User created successfully.");
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
