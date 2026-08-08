package com.menzo.User_Service.Wishlist.Repository;

import com.menzo.User_Service.Wishlist.Entity.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {

    /*
     *
     *   Existence check for wishlist-item
     *   Checked by product-item SKU
     *
     */
    boolean existsByWishlist_WishlistIdAndProductItemSku(UUID wishlistId, String productItemSku);


    /*
     *
     *   Find wishlist-item by SKU
     *
     */
    Optional<WishlistItem> findByWishlist_WishlistIdAndProductItemSku(UUID wishlistId, String productItemSku);


    /*
     *
     *   Find all wishlist-items of customer
     *   With pagination and createdAt sorting
     *
     */
    Page<WishlistItem> findAllByWishlist_WishlistId(UUID wishlistId, Pageable sortedPageable);

}
