package com.menzo.User_Service.Wishlist.Controller;

import com.menzo.User_Service.Wishlist.Service.WishlistQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
public class WishlistQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistQueryRestController.class);

    @Autowired
    private WishlistQueryService wishlistQueryService;


    /*
     *
     *   Get all wishlist-items with pagination
     *   Wishlist-items sorted by createdAt (latest first)
     *
     */
    @GetMapping("/get")
    public ResponseEntity<?> getAllWishlistItemsWithPagination(@RequestHeader("email") String userEmail,
                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        //  validating inputs
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be less than 0.");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be 0 to 100");
        }

        //  getting cart-items
        Page<?> pageContent = wishlistQueryService.getWishlistItemsWithPagination(
                userEmail,
                page,
                size
        );

        //  building response
        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent != null) {
            responseBody.put("message", "Wishlist-items fetched successfully");
            responseBody.put("pageContent", pageContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            responseBody.put("message", "Wishlist-items fetching failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }

}
