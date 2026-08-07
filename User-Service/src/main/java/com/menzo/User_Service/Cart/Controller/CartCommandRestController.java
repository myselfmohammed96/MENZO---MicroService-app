package com.menzo.User_Service.Cart.Controller;

import com.menzo.User_Service.Cart.Service.CartCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(CartCommandRestController.class);

    @Autowired
    private CartCommandService cartCommandService;


    /*
     *
     *   Add new product-item in cart
     *
     */
    @PostMapping("/add")
    public ResponseEntity<?> addNewCartItem(@RequestHeader("roles") String roles,
                                            @RequestHeader("email") String userEmail,
                                            @RequestParam("id") Long productItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (productItemId == null || productItemId <= 0) {
                logger.warn("Invalid product-item ID: {}", productItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }

            //  Adding item to cart
            boolean itemAdded = cartCommandService.addNewCartItem(userEmail, productItemId);

            //  building response
            if (itemAdded) {
                logger.info("Product-item '{}', added to the cart successfully", productItemId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Product-item added to the cart successfully"));
            } else {
                logger.error("Failed to add product-item '{}' to the cart", productItemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Failed to add product-item to the cart."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update cart-item quantity
     *
     */
    @PatchMapping("/update-quantity")
    public ResponseEntity<?> updateCartItemQuantity(@RequestHeader("roles") String roles,
                                                    @RequestHeader("email") String userEmail,
                                                    @RequestParam("id") UUID cartItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (cartItemId == null) {
                logger.warn("Invalid cart-item ID: {}", cartItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid cart-item ID"));
            }

            //  updating cart quantity
            UUID customerId = cartCommandService.updateCartItemQuantity(userEmail, cartItemId);

            //  building response
            if (customerId != null) {
                logger.info("Cart-item quantity for product item ID '{}', updated successfully for customer ID: {}", cartItemId, customerId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Cart-item quantity updated successfully"));
            } else {
                logger.error("Cart-item quantity update for ID '{}' failed for customer ID: {}", cartItemId, customerId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Cart-item quantity update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Move cart-item to wishlist
     *
     */
    @PatchMapping("/move-to-wishlist")
    public ResponseEntity<?> moveCartItemToWishlist(@RequestHeader("roles") String roles,
                                                    @RequestHeader("email") String userEmail,
                                                    @RequestParam("id") UUID cartItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (cartItemId == null) {
                logger.warn("Invalid cart-item ID: {}", cartItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid cart-item ID"));
            }

            //  moving cart item to wishlist
            boolean itemMoved = cartCommandService.moveCartItemToWishlist(userEmail, cartItemId);

            //  building response
            if (itemMoved) {
                logger.info("Cart-item with ID '{}', moved successfully to the wishlist", cartItemId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Cart-item moved successfully to the wishlist"));
            } else {
                logger.error("Failed to move cart-item with ID '{}' to the wishlist", cartItemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Failed to move cart-item to wishlist."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete cart item (soft delete)
     *
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCartItem(@RequestHeader("roles") String roles,
                                            @RequestHeader("email") String userEmail,
                                            @RequestParam("id") UUID cartItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (cartItemId == null) {
                logger.warn("Invalid cart-item ID: {}", cartItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid cart-item ID"));
            }

            //  deleting cart item
            boolean deleted = cartCommandService.deleteCartItem(userEmail, cartItemId);

            //  building response
            if (deleted) {
                logger.info("Cart-item '{}', deleted successfully", cartItemId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Cart-item deleted successfully"));
            } else {
                logger.error("Cart-item '{}' deletion failed", cartItemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Cart-item deletion failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}