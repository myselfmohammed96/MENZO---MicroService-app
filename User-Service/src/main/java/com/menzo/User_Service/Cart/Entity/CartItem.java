package com.menzo.User_Service.Cart.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "cart_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_item",
                        columnNames = {"cart_id", "product_item_id"}
                )
        }
)
@FilterDef(
        name = "cartFilter",
        parameters = {
                @ParamDef(name = "isOrdered", type = Boolean.class),
                @ParamDef(name = "movedToWishlist", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "cartFilter",
        condition = "is_ordered = :isOrdered AND moved_to_wishlist = :movedToWishlist AND is_deleted = :isDeleted"
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(nullable = false)
    private Long productItemId;

    @Column(nullable = false)
    private String productItemSku;

    private Integer quantity = 1;

    @Column(nullable = false)
    private boolean isOrdered = false;

    @Column(nullable = false)
    private boolean movedToWishlist = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime movedToWishlistAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
