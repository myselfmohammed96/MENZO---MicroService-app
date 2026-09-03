package com.menzo.User_Service.WishlistCart.Common.Service;

import com.menzo.User_Service.Exceptions.UnauthorizedAccessException;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import com.menzo.User_Service.WishlistCart.Cart.Entity.CartItem;
import com.menzo.User_Service.WishlistCart.Cart.Repository.CartItemRepository;
import com.menzo.User_Service.WishlistCart.Cart.Service.CartCommandService;
import com.menzo.User_Service.WishlistCart.Wishlist.Entity.WishlistItem;
import com.menzo.User_Service.WishlistCart.Wishlist.Repository.WishlistItemRepository;
import com.menzo.User_Service.WishlistCart.Wishlist.Service.WishlistCommandService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class WishlistCartCommandService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistCartCommandService.class);

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private WishlistCommandService wishlistCommandService;

    @Autowired
    private CartCommandService cartCommandService;

    @Autowired
    private WishlistItemRepository wishlistItemRepo;

    @Autowired
    private CartItemRepository cartItemRepo;


    /*
     *
     *   Move wishlist-item to cart
     *
     */
    public boolean moveWishlistItemToCart(String userEmail,
                                          UUID wishlistItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching wishlist-item
        WishlistItem wishlistItem = wishlistItemRepo.findById(wishlistItemId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist-item not found with ID: " + wishlistItemId));

        //  checking wishlist-item belongs to authenticated user
        if (!Objects.equals(user.getUserId(),
                wishlistItem.getWishlist().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: wishlist-item does not belong to the authenticated user.");
        }

        //  move item to cart
        boolean movedToCart = cartCommandService.moveWishlistItemToCart(
                user,
                wishlistItem.getProductItemId(),
                wishlistItem.getProductItemSku()
        );

        //  mark wishlist-item as moved
        if (movedToCart) {
            wishlistItem.setMovedToCart(true);
            wishlistItem.setMovedToCartAt(LocalDateTime.now());
            wishlistItemRepo.save(wishlistItem);
            return true;
        } else {
            return false;
        }
    }


    /*
     *
     *   Move cart-item to wishlist
     *
     */
    public boolean moveCartItemToWishlist(String userEmail,
                                          UUID cartItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching cart-item
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart-item not found with ID: " + cartItemId));

        //  checking cart-item belongs to authenticated user
        if (!Objects.equals(user.getUserId(),
                cartItem.getCart().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: cart-item does not belong to the authenticated user.");
        }

        //  move item to wishlist
        boolean movedToWishlist = wishlistCommandService.moveCartItemToWishlist(
                user,
                cartItem.getProductItemId(),
                cartItem.getProductItemSku()
        );

        //  mark cart-item as moved
        if (movedToWishlist) {
            cartItem.setMovedToWishlist(true);
            cartItem.setMovedToWishlistAt(LocalDateTime.now());
            cartItemRepo.save(cartItem);
            return true;
        } else {
            return false;
        }
    }

}
