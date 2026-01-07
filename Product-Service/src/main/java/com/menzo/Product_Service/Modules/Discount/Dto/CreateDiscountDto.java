package com.menzo.Product_Service.Modules.Discount.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import jakarta.validation.constraints.*;
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
//@ValidDiscountDates
//@ValidDiscountCap
public class CreateDiscountDto {

    @NotBlank(message = "Discount code is required")
    @Size(
            min = 3,
            max = 50,
            message = "Discount code must be 3 to 50 characters long"
    )
    private String discountCode;

    @NotBlank(message = "Discount name is required")
    @Size(
            min = 5,
            max = 100,
            message = "Discount name must be 5 to 100 characters long"
    )
    private String discountName;

    @Size(
            max = 255,
            message = "Discount description must not exceed 255 characters"
    )
    private String discountDescription;

    @NotNull(message = "Discount level is required")
    private DiscountLevel level;

    @NotNull(message = "Discount type is required")
    private DiscountType type;

    @NotNull(message = "Discount status is required")
    private PromotionStatus discountStatus;

    @NotNull(message = "Discount value is required")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Discount value must be greater than 0"
    )
    private BigDecimal value;

    @NotNull(message = "Cap type is required")
    private CapType capType;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Cap value must be greater that 0"
    )
    private BigDecimal capValue;

    @Min(
            value = 0,
            message = "Discount priority should not be less than 0"
    )
    private Integer priority;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    @JsonFormat(pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime startAt;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    @JsonFormat(pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime endAt;

}
