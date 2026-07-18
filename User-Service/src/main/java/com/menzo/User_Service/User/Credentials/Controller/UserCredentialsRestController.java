package com.menzo.User_Service.User.Credentials.Controller;

import com.menzo.User_Service.User.Credentials.Dto.ChangePasswordDto;
import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Credentials.Service.CredentialsService;
import com.menzo.User_Service.User.Profile.Service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserCredentialsRestController {

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private CredentialsService credentialsService;



    /*
    *
    *   User email existence check
    *   for user sign-in validation
    *
    */
    @PostMapping("/is-exists")
    public ResponseEntity<Map<String, Boolean>> isUserEmailExists(@RequestBody EmailDto emailDto) {
        boolean emailExists = userQueryService.isUserEmailExists(emailDto);
        return ResponseEntity.ok(Map.of("exists", emailExists));
    }



    /*
    *
    *   Update user password
    *   User identified by user email
    *
    */
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestHeader("loggedInUser") String userEmail,
                                            @RequestParam("present") boolean passwordPresent,
                                            @RequestBody ChangePasswordDto passwordDto) {
        if (passwordDto == null ||
            passwordDto.getNewPassword() == null || passwordDto.getNewPassword().isEmpty() ||
            passwordDto.getConfirmPassword() == null || passwordDto.getConfirmPassword().isEmpty() ||
            !Objects.equals(passwordDto.getNewPassword(), passwordDto.getConfirmPassword()) ||
            (passwordPresent && (passwordDto.getCurrentPassword() == null || passwordDto.getCurrentPassword().isEmpty()))) {
            throw new IllegalArgumentException("Password dto invalid.");
        }
        boolean passwordUpdated = credentialsService.updatePassword(userEmail, passwordPresent, passwordDto);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", passwordUpdated));
    }
}
