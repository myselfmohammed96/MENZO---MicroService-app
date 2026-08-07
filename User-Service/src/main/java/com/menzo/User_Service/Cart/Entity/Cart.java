package com.menzo.User_Service.Cart.Entity;

import com.menzo.User_Service.Customer.Entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

}
