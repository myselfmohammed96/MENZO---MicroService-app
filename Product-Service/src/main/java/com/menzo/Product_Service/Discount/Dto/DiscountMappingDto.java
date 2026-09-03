package com.menzo.Product_Service.Discount.Dto;

import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountMappingDto {

    @NotNull(message = "Discount Id is required")
    private UUID discountId;

    @NotNull(message = "Discount level is required")
    private DiscountLevel level;

    @NotEmpty(message = "Selection list cannot be empty")
    private List<UUID> selectionList;

}
