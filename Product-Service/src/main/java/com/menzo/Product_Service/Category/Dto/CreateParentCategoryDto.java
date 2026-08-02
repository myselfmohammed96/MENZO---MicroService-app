package com.menzo.Product_Service.Category.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateParentCategoryDto {

    @NotBlank(message = "Category name is required")
    private String categoryName;

}
