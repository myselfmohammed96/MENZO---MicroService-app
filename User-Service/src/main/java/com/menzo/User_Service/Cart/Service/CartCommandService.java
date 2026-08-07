package com.menzo.User_Service.Cart.Service;

import com.menzo.User_Service.Cart.Entity.Cart;
import com.menzo.User_Service.Cart.Entity.CartItem;
import com.menzo.User_Service.Cart.Repository.CartItemRepository;
import com.menzo.User_Service.Cart.Repository.CartRepository;
import com.menzo.User_Service.Customer.Entity.Customer;
import com.menzo.User_Service.Feign.ProductFeign;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public boolean addNewCartItem(String userEmail,
                                  Long productItemId) {
        //  fetching customer cart
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Cart cart = user.getCustomer().getCart();

        //  fetching product-item SKU
        //  ## Handle null SKU if the Feign call could fail.
        String sku = productFeign.getSkuByItemId(productItemId).getBody();

        //  increment cart-item quantity, if product-item is already in the cart
        Optional<CartItem> cartItemOpt = cartItemRepo.findByCart_CartIdAndProductItemSku(cart.getCartId(), sku);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepo.save(cartItem);
            return true;
        }

        //  adding new cart-item
        CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .productItemId(productItemId)
                .productItemSku(sku)
                .build();
        cartItemRepo.save(newCartItem);
        return true;
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

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepo.save(cartItem);
        return user.getCustomer().getCustomerId();
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

        return true;
    }


    /*
     *
     *   Move wishlist-item to cart
     *
     */
    public boolean moveWishlistItemToCart(Long id) {

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

        //  deleting cart-item
        cartItem.setDeleted(true);
        cartItem.setDeletedAt(LocalDateTime.now());
        return true;
    }

}

