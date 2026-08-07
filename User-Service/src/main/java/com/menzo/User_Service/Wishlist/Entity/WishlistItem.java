package com.menzo.User_Service.Wishlist.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wishlist_items")
public class WishlistItem {
}
