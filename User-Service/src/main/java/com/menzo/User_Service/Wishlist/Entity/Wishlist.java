package com.menzo.User_Service.Wishlist.Entity;

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
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID wishlistId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

}
