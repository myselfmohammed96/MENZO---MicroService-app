package com.menzo.User_Service.Controller;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Service.UserRetrievalService;
import com.menzo.User_Service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @Autowired
    UserRetrievalService userRetrievalService;

    //  Get user by email - for IDENTITY-SERVICE (id, firstName, lastName, email, phoneNumber, password, roles, isActive)
    @PostMapping("/get-by-email")
    public ResponseEntity<UserDto> getUserbyUserEmail(@RequestBody EmailDto userEmail) {
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
}
