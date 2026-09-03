package com.menzo.User_Service.WishlistCart.Wishlist.Entity;

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
        name = "wishlists",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_customer_wishlist",
                columnNames = "customer_id"
        )
)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID wishlistId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_wishlist")
    )
    private Customer customer;

    @OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
    private List<WishlistItem> wishlistItems;

}
