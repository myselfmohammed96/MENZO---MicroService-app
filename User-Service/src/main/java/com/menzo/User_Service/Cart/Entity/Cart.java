package com.menzo.User_Service.Cart.Entity;

import com.menzo.User_Service.Customer.Entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "carts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_customer_cart",
                columnNames = "customer_id"
        )
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_cart")
    )
    private Customer customer;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

}
