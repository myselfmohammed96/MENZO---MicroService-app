package com.menzo.User_Service.Cart.Service;

import com.menzo.User_Service.Cart.Dto.CartDto;
import com.menzo.User_Service.Cart.Entity.Cart;
import com.menzo.User_Service.Cart.Entity.CartItem;
import com.menzo.User_Service.Cart.Repository.CartItemRepository;
import com.menzo.User_Service.Feign.ProductFeign;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CartQueryService.class);

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ProductFeign productFeign;


    /*
     *
     *   Get all cart-items with pagination
     *   Cart-items sorted by createdAt (latest first)
     *
     */
    public Page<?> getCartItemsWithPagination(String userEmail, Integer page, Integer size) {

        //  fetching user cart
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Cart cart = user.getCustomer().getCart();

        //  getting cart page
        Pageable sortedPageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "addedAt")
        );
        Page<CartItem> cartItems = cartItemRepo.findAllByCart_CartId(
                cart.getCartId(),
                sortedPageable
        );

        //  getting cart page data
        List<CartItem> pageContent = cartItems.getContent();
        List<UUID> cartItemIds = pageContent.stream()
                .map(CartItem::getProductItemId)
                .toList();

        List<CartDto> cartItemData = productFeign.getCartItemsData(cartItemIds).getBody();
        if (cartItemData == null) {
            throw new RuntimeException();
        }

        return new PageImpl<>(cartItemData, sortedPageable, cartItems.getTotalElements());
    }


    /*
     *
     *   Get cart-items count
     *
     */
    public Long getCartItemCount(String userEmail) {

        //  fetching user cart
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Cart cart = user.getCustomer().getCart();

        //  count all cart-items in customer cart
        return cartItemRepo.countByCart_CartId(cart.getCartId());
    }

}
