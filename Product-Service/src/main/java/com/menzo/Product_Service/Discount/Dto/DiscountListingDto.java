package com.menzo.Product_Service.Discount.Dto;

import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Discount.Enum.OperationalStatus;
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

    private OperationalStatus status;

}
