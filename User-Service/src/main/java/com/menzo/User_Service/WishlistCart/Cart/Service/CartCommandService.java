package com.menzo.User_Service.WishlistCart.Cart.Service;

import com.menzo.User_Service.WishlistCart.Cart.Entity.Cart;
import com.menzo.User_Service.WishlistCart.Cart.Entity.CartItem;
import com.menzo.User_Service.WishlistCart.Cart.Repository.CartItemRepository;
import com.menzo.User_Service.Exceptions.UnauthorizedAccessException;
import com.menzo.User_Service.Feign.ProductFeign;
import com.menzo.User_Service.GlobalComponents.Enum.Response;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import com.menzo.User_Service.WishlistCart.Wishlist.Service.WishlistCommandService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartCommandService {

    private static final Logger logger = LoggerFactory.getLogger(CartCommandService.class);

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ProductFeign productFeign;


    /*
     *
     *   Add new product-item in cart
     *
     */
    @Transactional
    public Response addNewCartItem(String userEmail,
                                   UUID productItemId) {
        //  fetching customer cart
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Cart cart = user.getCustomer().getCart();

        //  fetching product-item by SKU
        String sku = productFeign.getSkuByItemId(productItemId).getBody();
        if (sku == null) {
            throw new EntityNotFoundException("Product-item SKU not found for product-item ID: " + productItemId);
        }


        //  fetching cart-item if exist
        Optional<CartItem> cartItemOpt = cartItemRepo.findByCart_CartIdAndProductItemSku(cart.getCartId(), sku);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            //  increment cart-item quantity, if product-item already exist in the cart
            if (!cartItem.isOrdered() && !cartItem.isMovedToWishlist() && !cartItem.isDeleted()) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemRepo.save(cartItem);
                return Response.INCREMENTED;
            }

            //  undo isOrdered, isDeleted & isMovedToWishlist and refresh addAt
            if (cartItem.isOrdered()) {
                cartItem.setOrdered(false);
            }
            if (cartItem.isDeleted()) {
                cartItem.setDeleted(false);
            }
            if (cartItem.isMovedToWishlist()) {
                cartItem.setMovedToWishlist(false);
            }
            cartItem.setAddedAt(LocalDateTime.now());
            cartItemRepo.save(cartItem);

            return Response.RESTORED;
        }

        //  adding new cart-item
        CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .productItemId(productItemId)
                .productItemSku(sku)
                .addedAt(LocalDateTime.now())
                .build();
        try {
            cartItemRepo.save(newCartItem);
            return Response.CREATED;
        } catch (Exception e) {
            logger.error("Failed to save new item to cart: ", e);
            return Response.FAILED;
        }
    }


    /*
     *
     *   Update cart-item quantity
     *
     */
    public UUID updateCartItemQuantity(String userEmail,
                                       UUID cartItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching cart-item
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart-item not found with ID: " + cartItemId));

        //  checking cart-item belongs to authenticated user
        if (Objects.equals(user.getUserId(),
                cartItem.getCart().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: cart-item does not belong to the authenticated user.");
        }

        //  updating cart-item quantity
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepo.save(cartItem);
        return user.getCustomer().getCustomerId();
    }


    /*
     *
     *   Move wishlist-item to cart
     *   Used by WishlistCommandService
     *
     */
    public boolean moveWishlistItemToCart(User user,
                                          UUID productItemId,
                                          String productItemSku) {
        //  fetching user cart
        Cart userCart = user.getCustomer().getCart();

        //  checking if product-item already exists in cart
        Optional<CartItem> cartItemOpt = cartItemRepo.findByCart_CartIdAndProductItemSku(
                userCart.getCartId(),
                productItemSku
        );

        //  reuse cart-item if already present
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            if (cartItem.isMovedToWishlist()) {
                cartItem.setMovedToWishlist(false);
                cartItem.setQuantity(1);
                cartItemRepo.save(cartItem);
            }
            return true;
        }

        //  create new cart-item if not already present
        CartItem newCartItem = CartItem.builder()
                .cart(userCart)
                .productItemId(productItemId)
                .productItemSku(productItemSku)
                .build();
        cartItemRepo.save(newCartItem);
        return true;
    }


    /*
     *
     *   Delete cart item (soft delete)
     *
     */
    public boolean deleteCartItem(String userEmail,
                                  UUID cartItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching cart-item
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart-item not found with ID: " + cartItemId));

        //  checking cart-item belongs to authenticated user
        if (Objects.equals(user.getUserId(),
                cartItem.getCart().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: cart-item does not belong to the authenticated user.");
        }

        //  deleting cart-item
        cartItem.setDeleted(true);
        cartItem.setDeletedAt(LocalDateTime.now());
        return true;
    }

}

