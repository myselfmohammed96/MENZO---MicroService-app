package com.menzo.User_Service.Cart.Repository;

import com.menzo.User_Service.Cart.Entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    /*
     *
     *   Existence check for cart-item
     *   Checked by product-item SKU
     *
     */
    boolean existsByCart_CartIdAndProductItemSku(UUID cartId, String productItemSku);


    /*
     *
     *   Find cart-item by SKU
     *
     */
    Optional<CartItem> findByCart_CartIdAndProductItemSku(UUID cartId, String productItemSku);


    /*
     *
     *   Find all cart-items of customer
     *   With pagination and createdAt sorting
     *
     */
    Page<CartItem> findAllByCart_CartId(UUID cartId, Pageable sortedPageable);


    /*
     *
     *   Count all cart-items in customer cart
     *
     */
    long countByCart_CartId(UUID cartId);

}
