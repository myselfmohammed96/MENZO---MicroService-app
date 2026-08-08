package com.menzo.User_Service.Cart.Service;

import com.menzo.User_Service.Cart.Repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CartQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CartQueryService.class);

    @Autowired
    private CartItemRepository cartItemRepo;


    /*
     *
     *   Get all cart-items with pagination
     *   Cart-items sorted by createdAt (latest first)
     *
     */
    public Page<?> getCartItemsWithPagination(String userEmail, Integer page, Integer size) {
    }


    /*
     *
     *   Get cart-items count
     *
     */
    public Integer getCartItemCount(String userEmail) {
    }

}
