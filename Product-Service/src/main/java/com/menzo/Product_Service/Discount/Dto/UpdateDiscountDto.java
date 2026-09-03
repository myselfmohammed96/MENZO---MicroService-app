package com.menzo.Product_Service.Discount.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Product_Service.Discount.Enum.CapType;
import com.menzo.Product_Service.Discount.Enum.OperationalStatus;
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

    private BigDecimal discountValue;

    private CapType capType;

    private BigDecimal capValue;

    private Integer priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime resumeAt;

    private OperationalStatus discountStatus;

}
