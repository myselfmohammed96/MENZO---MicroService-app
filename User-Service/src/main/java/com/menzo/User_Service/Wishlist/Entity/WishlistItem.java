package com.menzo.User_Service.Wishlist.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
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
        name = "wishlist_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk-wishlist-item",
                        columnNames = {"wishlist_id", "product_item_id"}
                )
        }
)
@FilterDef(
        name = "wishlistItemFilter",
        parameters = {
                @ParamDef(name = "applyMovedToCart", type = Boolean.class),
                @ParamDef(name = "movedToCart", type = Boolean.class),
                @ParamDef(name = "applyDeleted", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "wishlistItemFilter",
        condition = "(:applyMovedToCart = false OR moved_to_cart = :movedToCart) " +
                "AND (:applyDeleted = false OR is_deleted = :isDeleted) "
)
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID wishlistItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "wishlist_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_wishlist")
    )
    private Wishlist wishlist;

    @Column(nullable = false)
    private UUID productItemId;

    @Column(nullable = false)
    private String productItemSku;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private boolean movedToCart = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime movedToCartAt;

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
