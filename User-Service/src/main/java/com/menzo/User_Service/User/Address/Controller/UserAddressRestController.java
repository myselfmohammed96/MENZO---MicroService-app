package com.menzo.User_Service.User.Address.Controller;

import com.menzo.User_Service.User.Address.Dto.UserAddressDto;
import com.menzo.User_Service.User.Address.Service.UserAddressCommandService;
import com.menzo.User_Service.User.Address.Service.UserAddressQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserAddressRestController {

    @Autowired
    private UserAddressCommandService addressCommandService;

    @Autowired
    private UserAddressQueryService addressQueryService;



    /*
    *
    *   Get all address of user
    *   User identified by user email
    *
    */
    @GetMapping("/user-address")
    public ResponseEntity<?> getAllAddressByEmail(@RequestHeader("loggedInUser") String userEmail) {
        List<UserAddressDto> userAddresses = addressQueryService.getAllAddressByEmail(userEmail);

        Map<String, Object> response = new HashMap<>();
        if (userAddresses != null) {
            response.put("success", true);
            response.put("data", Map.of("userAddresses", userAddresses));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "User addresses fetching failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    /*
    *
    *   Get default address of user
    *   User identified by user email
    *
    */
    @GetMapping("/default-address")
    public ResponseEntity<?> getDefaultAddressByEmail(@RequestHeader("loggedInUser") String userEmail) {
        UserAddressDto userAddress = addressQueryService.getDefaultAddressByEmail(userEmail);

        Map<String, Object> response = new HashMap<>();
        if (userAddress != null) {
            response.put("success", true);
            response.put("data", Map.of("userAddress", userAddress));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "User addresses fetching failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    /*
    *
    *   Add new user address
    *   User identified by user email
    *
    */
    @PostMapping("/address")
    public ResponseEntity<?> addUserAddress(@RequestHeader("loggedInUser") String userEmail,
                                            @RequestBody UserAddressDto userAddress) {
        UUID id = addressCommandService.addUserAddress(
                userEmail,
                userAddress
        );
        Map<String, Object> response = new HashMap<>();
        if(id != null) {
            response.put("success", true);
            response.put("data", Map.of("id", id));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "User address adding failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    /*
    *
    *   Update user address
    *   User identified by user email
    *
    */
    @PutMapping("/address")
    public ResponseEntity<?> updateUserAddress(@RequestHeader("loggedInUser") String userEmail,
                                               @RequestParam("id") UUID addressId,
                                               @RequestBody UserAddressDto userAddress) {
        UUID userAddressId = addressCommandService.updateUserAddress(
                userEmail,
                addressId,
                userAddress
        );
        Map<String, Object> response = new HashMap<>();
        if(userAddressId != null) {
            response.put("success", true);
            response.put("data", Map.of("id", userAddressId));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "User address update failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    /*
    *
    *   Delete user address
    *   (soft delete)
    *   User identified by user email
    *
    */
    @DeleteMapping("/address")
    public ResponseEntity<?> deleteUserAddress(@RequestHeader("loggedInUser") String userEmail,
                                               @RequestParam("id") Long addressId) {
        addressCommandService.deleteUserAddress(
                userEmail,
                addressId
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
