package com.menzo.User_Service.Cart.Controller;

import com.menzo.User_Service.Cart.Service.CartQueryService;
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
@RequestMapping("/cart")
public class CartQueryRestController {

    private static Logger logger = LoggerFactory.getLogger(CartQueryRestController.class);

    @Autowired
    private CartQueryService cartQueryService;


    /*
     *
     *   Get all cart-items with pagination
     *   Cart-items sorted by createdAt (latest first)
     *
     */
    @GetMapping("/get")
    public ResponseEntity<?> getAllCartItemsWithPagination(@RequestHeader("email") String userEmail,
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
        Page<?> pageContent = cartQueryService.getCartItemsWithPagination(
                userEmail,
                page,
                size
        );

        //  building response
        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent != null) {
            responseBody.put("message", "Cart-items fetched successfully");
            responseBody.put("pageContent", pageContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            responseBody.put("message", "Cart-items fetching failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *
     *   Get cart-items count
     *
     */
    @GetMapping("/get-count")
    public ResponseEntity<?> getCartItemCount(@RequestHeader("email") String userEmail) {

        //  getting cart-item count
        Integer cartItemCount = cartQueryService.getCartItemCount(userEmail);

        //  building response
        return ResponseEntity.ok(cartItemCount);
    }

}
