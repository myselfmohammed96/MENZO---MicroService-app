package com.menzo.User_Service.User.UserProfile.Controller;

import com.menzo.User_Service.User.UserProfile.Dto.BlockUserDto;
import com.menzo.User_Service.User.UserProfile.Dto.UserDto;
import com.menzo.User_Service.User.UserProfile.Service.UserCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserCommandRestController.class);

    @Autowired
    private UserCommandService userCommandService;



    /*
    *
    *   Update user active status
    *
    */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateUserActiveStatus(@RequestBody BlockUserDto blockUser) {
        userCommandService.updateUserActiveStatus(blockUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "User activeStatus updated successfully"));
    }



    /*
    *
    *   Update user details
    *   User identified by user email
    *
    */
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUserDetailsByEmail(@RequestHeader("loggedInUser") String userEmail,
                                                      @RequestBody UserDto latestUserDetails) {
        if(latestUserDetails == null) {
            logger.error("User update details unavailable: {}", userEmail);
            throw new IllegalArgumentException("User update details unavailable: " + userEmail);
        }
        UUID updatedUserId = userCommandService.updateUserDetails(userEmail, latestUserDetails);

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
}
