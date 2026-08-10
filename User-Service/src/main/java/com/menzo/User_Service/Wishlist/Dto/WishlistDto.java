package com.menzo.User_Service.Wishlist.Dto;

import com.menzo.User_Service.GlobalComponents.Enum.DiscountType;
import com.menzo.User_Service.GlobalComponents.Enum.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDto {

    private UUID wishlistItemId;

    private UUID productItemId;

    private String sku;

    private String productName;

    private String imageUrl;

    private StockStatus stockStatus;

    private BigDecimal sellingPrice;

    private BigDecimal mrp;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private LocalDateTime addedAt;

}
