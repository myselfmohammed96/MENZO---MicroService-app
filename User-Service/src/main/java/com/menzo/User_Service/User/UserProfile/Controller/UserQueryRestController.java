package com.menzo.User_Service.User.UserProfile.Controller;

import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.UserProfile.Dto.*;
import com.menzo.User_Service.User.UserProfile.Enum.Gender;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserQueryRestController {

    @Autowired
    private UserQueryService userQueryService;



    /*
    *
    *   Get user details
    *   User identified by user email
    *   API used for IDENTITY-SERVICE
    *
    */
    @PostMapping("/get-by-email")
    public ResponseEntity<UserDto> getUserbyUserEmail(@RequestBody EmailDto userEmail) {
        if (userEmail == null || userEmail.getEmail().isEmpty()) {
            throw new IllegalArgumentException("user email unavailable");
        }
        UserDto user = userQueryService.getUserbyEmail(userEmail);
        return ResponseEntity.ok(user);
    }



    /*
    *
    *   Get user status
    *   User identified by user ID
    *
    */
    @GetMapping("/get-by-id")
    public ResponseEntity<UserStatusDto> getUserStatusByUserId(@RequestParam("id") Long userId) {
        UserStatusDto userDto = userQueryService.getUserStatusByUserId(userId);
        return ResponseEntity.ok(userDto);
    }



    /*
    *
    *   Get user details
    *   User identified by user email
    *   API used for HOME-SERVICE (client side)
    *
    */
    @PostMapping("/get-user-for-client-side")
    public ResponseEntity<?> getUserDetailsForClientSide(@RequestBody EmailDto userEmail) {
        ClientSideUserDetailsDto user = userQueryService.getUserDetailsForClientSide(userEmail);
        return ResponseEntity.ok(user);
    }



    /*
    *
    *   Get users list with pagination
    *   API used for ADMIN-SERVICE
    *
    */
    @GetMapping("/users-listing")
    public ResponseEntity<Page<UserListingDto>> getUsersListWithPagination(@RequestParam(defaultValue = "0") Integer page,
                                                                           @RequestParam(defaultValue = "15") Integer size) {
        Page<UserListingDto> usersPage = userQueryService.getUsersListWithPagination(page, size);
        return ResponseEntity.ok(usersPage);
    }



    /*
    *
    *   Get user details
    *   User identified by user ID
    *   API used for ADMIN-SERVICE
    *
    */
    @GetMapping("user-details")
    public ResponseEntity<UserDetailsDto> getUserDetailsById(@RequestParam("id") Long userId) {
        UserDetailsDto userDetails = userQueryService.getUserDetailsByIdForAdminSide(userId);
        return ResponseEntity.ok(userDetails);
    }



    /*
    *
    *   Get user genders
    *   API used for ** which service **
    *
    */
    @GetMapping("/user-gender")
    public ResponseEntity<Gender[]> getUserGender() {
        return ResponseEntity.ok(Gender.values());
    }
}
