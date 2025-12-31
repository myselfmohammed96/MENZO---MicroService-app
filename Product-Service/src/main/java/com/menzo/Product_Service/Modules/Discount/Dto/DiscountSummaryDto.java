package com.menzo.Product_Service.Modules.Discount.Dto;

import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
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
public class DiscountSummaryDto {

    private UUID discountId;

    private String discountCode;

    private String discountName;

    private String discountDescription;

    private DiscountLevel level;

    private DiscountType type;

    private BigDecimal value;

    private CapType capType;

    private BigDecimal capValue;

    private Integer priority;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private PromotionStatus status;

    private LocalDateTime resumeAt;

//    private String timeZone;

    private LocalDateTime createdAt;

//    private String createdBy;

    private LocalDateTime updatedAt;

//    private String updatedBy;

}
