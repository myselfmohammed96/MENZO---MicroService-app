package com.menzo.Product_Service.Modules.Discount.Dto;

import com.menzo.Product_Service.Modules.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountListingDto {

    private UUID discountId;

    private String discountCode;

    private String discountName;

    private DiscountLevel level;

    private DiscountType type;

    private BigDecimal value;

    private PromotionStatus status;

}
