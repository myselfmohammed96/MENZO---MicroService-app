package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Gender;
import com.menzo.User_Service.Service.UserRetrievalService;
import com.menzo.User_Service.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserRetrievalService userRetrievalService;

    @PostMapping("user-signin")
    public ResponseEntity<?> createNewUser(@RequestBody RegNewUser newUser,
                                                HttpServletResponse response){
        Cookie jwtCookie = userService.registerNewUser(newUser);
        response.addCookie(jwtCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
    }















    // User email existence check - for user sign-in validation
    @PostMapping("/is-exists")
    public ResponseEntity<Map<String, Boolean>> isUserEmailExists(@RequestBody EmailDto emailDto) {
        boolean emailExists = userService.isUserEmailExists(emailDto);
        return ResponseEntity.ok(Map.of("exists", emailExists));
    }

    //  Get user by email - for IDENTITY-SERVICE (id, firstName, lastName, email, phoneNumber, password, roles, isActive)
    @PostMapping("/get-by-email")
    public ResponseEntity<UserDto> getUserbyUserEmail(@RequestBody EmailDto userEmail) {
        if (userEmail == null || userEmail.getEmail().isEmpty()) {
            throw new IllegalArgumentException("user email unavailable");
        }
        System.out.println("From user rest controller: " + userEmail.getEmail());
        UserDto user = userRetrievalService.getUserbyEmail(userEmail);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<UserStatusDto> getUserByUserId(@RequestParam("id") Long userId) {
        UserStatusDto userDto = userRetrievalService.getUserByUserId(userId);
        return ResponseEntity.ok(userDto);
    }

    //  Get user by email for client side - for HOME-SERVICE (firstName, lastName, roles)
    @PostMapping("/get-user-for-client-side")
    public ResponseEntity<?> getUserDetailsForClientSide(@RequestBody EmailDto userEmail) {
        ClientSideUserDetailsDto user = userRetrievalService.getUserDetailsForClientSide(userEmail);
        return ResponseEntity.ok(user);
    }

    //  Get users list with pagination - for ADMIN-SERVICE (id, fullName, email, phoneNumber, activeStatus)
    @GetMapping("/users-listing")
    public ResponseEntity<Page<UserListingDto>> getUsersListWithPagination(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "15") Integer size) {
        Page<UserListingDto> usersPage = userRetrievalService.getUsersListWithPagination(page, size);
        return ResponseEntity.ok(usersPage);
    }

    //  Get user details by userId - for ADMIN-SERVICE ()
    @GetMapping("user-details")
    public ResponseEntity<UserDetailsDto> getUserDetailsById(@RequestParam("id") Long userId) {
        UserDetailsDto userDetails = userRetrievalService.getUserDetailsByIdForAdminSide(userId);
        return ResponseEntity.ok(userDetails);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateUserActiveStatus(@RequestBody UpdateUserActiveStatusDto statusDto) {
        User updatedUser = userService.updateUserActiveStatus(statusDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "User activeStatus updated successfully"));
    }




    @GetMapping("/user-gender")
    public ResponseEntity<Gender[]> getUserGender() {
//        GenderDto gender = userRetrievalService.getUserGender();
        return ResponseEntity.ok(Gender.values());
    }









//    =========

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUserDetailsByEmail(@RequestHeader("loggedInUser") String userEmail,
                                                      @RequestBody UserDto latestUserDetails) {
        if(latestUserDetails == null) {
            logger.error("User details unavailable while updating user: {}", userEmail);
            throw new IllegalArgumentException("User details unavailable while updating user: " + userEmail);
        }
        Long updatedUserId = userService.updateUserDetails(userEmail, latestUserDetails);

        Map<String, Object> response = new HashMap<>();
        if (updatedUserId != null) {
            response.put("success", true);
            response.put("data", Map.of("id", updatedUserId));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "User update failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

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
        boolean passwordUpdated = userService.updatePassword(userEmail, passwordPresent, passwordDto);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", passwordUpdated));
    }


//    ********* Google OAuth user *********

    @PostMapping("/google-oauth-access")
    public ResponseEntity<UserDto> googleOAuthUser(@RequestBody OAuthUserDto googleUser) {
//        System.out.println(googleUser.getUserName() + "\n" + googleUser.getEmail() + "\n" + googleUser.getProfileUrl());
        UserDto user = userService.saveGoogleOAuthUser(googleUser);

        return ResponseEntity.ok(user);
    }

}
