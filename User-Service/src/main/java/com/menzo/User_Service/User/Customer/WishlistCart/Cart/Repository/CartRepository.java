package com.menzo.User_Service.User.Customer.WishlistCart.Cart.Repository;

import com.menzo.User_Service.User.Customer.Entity.Customer;
import com.menzo.User_Service.User.Customer.WishlistCart.Cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    boolean existsByCustomer(Customer customer);
}
