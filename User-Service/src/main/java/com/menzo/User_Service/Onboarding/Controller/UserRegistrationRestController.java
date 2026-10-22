package com.menzo.User_Service.Onboarding.Controller;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
//import com.menzo.User_Service.Global.Dto.Response.SignupResponse;
import com.menzo.User_Service.Global.Util.MaskingUtil;
//import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.Onboarding.Dto.RegNewUser;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.SignupResponse;
import com.menzo.User_Service.Onboarding.Service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRegistrationRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);

    private final UserRegistrationService registrationService;

    @InitBinder
    void initBinder(WebDataBinder binder) {
        StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
        List.of("firstName", "lastName", "phoneNumber", "email", "redirectUrl")
                .forEach(field -> binder.registerCustomEditor(String.class, field, trimmer));
    }

    @PostMapping(
            value = "/signup",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @ModelAttribute RegNewUser request) {
        logger.info("POST /api/v1/users/signup received, email={}, hasProfilePic={}",
                MaskingUtil.maskEmail(request.getEmail()),
                request.getProfilePic() != null && !request.getProfilePic().isEmpty());

        SignupResponse data = registrationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("OTP_SENT",
                        "Account created. A verification code has been sent to your email.", data));
    }


























//    -------------------------------------------------

    //  save user details - on sign in - and return success
    //  front end - request otp and validate otp
    //  confirm sign in - redirect on success with jwt

    //  login - return jwt

    //  google sign in

    /*
     *
     *   Register new user (Customer)
     *   Form registration
     *
     */
//    @PostMapping("/sign-in")
//    public ResponseEntity<?> userSignup(@Valid @RequestBody RegNewUser newUser,
//                                           BindingResult result) {
////                                           HttpServletResponse response){
//
//        //  new user details validation
//        if (result.hasErrors()) {
//            Map<String, String> errors = new HashMap<>();
//            result.getFieldErrors().forEach(err ->
//                    errors.put(err.getField(), err.getDefaultMessage()));
//
//            logger.warn("Validation failed for new registered user: {}", errors);
//            return ResponseEntity.badRequest().body(errors);
//        }
//
//        System.out.println(newUser);
////        boolean otpSent = userRegistrationService.addNewUser(newUser);
//
//        //  response building
//        // ##show proper error message in the front end if the server returns error message for backend form validation
////        if (otpSent) {
//            logger.info("OTP sent to user email '{}' for user sign-in.", newUser.getEmail());
//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .build();
////        } else {
////            logger.info("Cannot send OTP to user email '{}' for user sign-in.", newUser.getEmail());
////            return ResponseEntity
////                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
////                    .build();
////        }
//    }



//    @PostMapping("/confirm-sign-in")
//    public ResponseEntity<?> confirmSignIn() {}
    //  user details saved
    //  redirect url saved
    //  otp sent

    //  if the user otp verification is successful
    //      verify true
    //      use redirect url and delete
    //      clear otp record
    //  if the user otp verification fails
    //      clear user details
    //      clear redirect url record
    //      clear otp record



        //  ## user enter otp
        //  ## if otp is valid - confirm reg user details as signed in
        //  then proceed further.

//        Cookie jwtCookie = userRegistrationService.registerNewUser(newUser);
//        response.addCookie(jwtCookie);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("User created successfully.");
//    }



    /*
    *
    *   Get Google OAuth user details
    *   For the user data got from the Google server
    *
    */
//    @PostMapping("/google-oauth-access")
//    public ResponseEntity<UserDto> googleOAuthUser(@RequestBody OAuthUserDto googleUser) {
//        UserDto user = registrationService.saveGoogleOAuthUser(googleUser);
//        return ResponseEntity.ok(user);
//    }
}
