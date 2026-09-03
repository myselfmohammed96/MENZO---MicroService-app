package com.menzo.User_Service.WishlistCart.Wishlist.Repository;

import com.menzo.User_Service.WishlistCart.Wishlist.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
}
