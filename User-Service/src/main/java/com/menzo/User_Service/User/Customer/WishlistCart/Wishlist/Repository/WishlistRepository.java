package com.menzo.User_Service.User.Customer.WishlistCart.Wishlist.Repository;

import com.menzo.User_Service.User.Customer.Entity.Customer;
import com.menzo.User_Service.User.Customer.WishlistCart.Wishlist.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    boolean existsByCustomer(Customer customer);
}
