package com.menzo.Product_Service.Category.Dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSubCategoryDto {

    @NotNull(message = "Parent category ID is required")
    private UUID parentCategoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private Set<UUID> variationIds = new HashSet<>();

}
