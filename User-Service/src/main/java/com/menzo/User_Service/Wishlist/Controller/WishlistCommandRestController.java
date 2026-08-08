package com.menzo.User_Service.Wishlist.Controller;

import com.menzo.User_Service.GlobalComponents.Enum.Response;
import com.menzo.User_Service.Wishlist.Service.WishlistCommandService;
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
@RequestMapping("/wishlist")
public class WishlistCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistCommandRestController.class);

    @Autowired
    private WishlistCommandService wishlistCommandService;


    /*
     *
     *   Add new product-item in wishlist
     *
     */
    @PostMapping("/add")
    public ResponseEntity<?> addNewWishlistItem(@RequestHeader("roles") String roles,
                                                @RequestHeader("email") String userEmail,
                                                @RequestParam("id") Long productItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (productItemId == null || productItemId <= 0) {
                logger.warn("Invalid product-item ID: {}", productItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }

            //  Adding item to wishlist
            Response itemAdded = wishlistCommandService.addNewWishlistItem(userEmail, productItemId);

            //  building response
            return switch (itemAdded) {
                case RESTORED, CREATED -> {
                    logger.info("Product-item '{}', added to the wishlist successfully", productItemId);
                    yield ResponseEntity.status(HttpStatus.CREATED)
                            .body(Map.of("message", "Product-item added to the wishlist successfully"));
                }
                case ALREADY_EXISTS -> {
                    logger.error("Product-item '{}' already exists in the wishlist", productItemId);
                    yield ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("message", "Product-item already exists in the wishlist."));
                }
                case FAILED -> {
                    logger.error("Failed to add product-item '{}' to the wishlist", productItemId);
                    yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("message", "Failed to add product-item to the wishlist."));
                }
                default -> ResponseEntity.internalServerError().build();
            };
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Move wishlist-item to cart
     *
     */
    @PatchMapping("/move-to-cart")
    public ResponseEntity<?> moveWishlistItemToCart(@RequestHeader("roles") String roles,
                                                    @RequestHeader("email") String userEmail,
                                                    @RequestParam("id") UUID wishlistItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (wishlistItemId == null) {
                logger.warn("Invalid wishlist-item ID: {}", wishlistItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid wishlist-item ID"));
            }

            //  moving wishlist item to cart
            boolean itemMoved = wishlistCommandService.moveWishlistItemToCart(userEmail, wishlistItemId);

            //  building response
            if (itemMoved) {
                logger.info("Wishlist-item with ID '{}', moved successfully to the cart", wishlistItemId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Wishlist-item moved successfully to the cart"));
            } else {
                logger.error("Failed to move wishlist-item with ID '{}' to the cart", wishlistItemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Failed to move wishlist-item to cart."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete wishlist-item (soft delete)
     *
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteWishlistItem(@RequestHeader("roles") String roles,
                                                @RequestHeader("email") String userEmail,
                                                @RequestParam("id") UUID wishlistItemId) {
        if (roles.equals("CUSTOMER")) {

            //  input validation
            if (wishlistItemId == null) {
                logger.warn("Invalid wishlist-item ID: {}", wishlistItemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid wishlist-item ID"));
            }

            //  deleting cart item
            boolean deleted = wishlistCommandService.deleteWishlistItem(userEmail, wishlistItemId);

            //  building response
            if (deleted) {
                logger.info("Wishlist-item '{}', deleted successfully", wishlistItemId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Wishlist-item deleted successfully"));
            } else {
                logger.error("Wishlist-item '{}' deletion failed", wishlistItemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Wishlist-item deletion failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
