package com.menzo.User_Service.Wishlist.Repository;

import com.menzo.User_Service.Wishlist.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
}
