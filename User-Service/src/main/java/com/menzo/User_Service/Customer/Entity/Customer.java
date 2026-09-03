package com.menzo.User_Service.Customer.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.WishlistCart.Cart.Entity.Cart;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.WishlistCart.Wishlist.Entity.Wishlist;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "customers",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_customer_user",
                columnNames = "user_id"
        )
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_user")
    )
    private User user;

    private Integer loyaltyPoints;

    private Integer totalOrders;

    private BigDecimal totalSpent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastOrderAt;

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Wishlist wishlist;

//    private String membershipLevel;   //  Regular, Silver, Gold, VIP

//    private boolean marketingConsent;

//    private List<String> preferredSizes;

//    private List<String> preferredColor;

//    private String preferredFit;

}


/*
 *
 *   marketingConsent -> whether the customer has agreed to receive promotional emails/SMS or not.
 *   (offers, discounts, new arrivals)
 *
 */
