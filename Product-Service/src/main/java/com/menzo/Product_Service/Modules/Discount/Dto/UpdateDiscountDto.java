package com.menzo.Product_Service.Modules.Discount.Dto;

import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDiscountDto {

    private String discountName;

    private String discountDescription;

    private BigDecimal value;

    private CapType capType;

    private BigDecimal capValue;

    private Integer priority;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime resumeAt;

    private PromotionStatus discountStatus;

}
