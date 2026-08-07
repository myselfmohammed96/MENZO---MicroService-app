package com.menzo.User_Service.Cart.Repository;

import com.menzo.User_Service.Cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
