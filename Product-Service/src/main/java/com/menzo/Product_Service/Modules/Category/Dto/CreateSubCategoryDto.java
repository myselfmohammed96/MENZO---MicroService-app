package com.menzo.Product_Service.Modules.Category.Dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    private Long id;

    @NotNull(message = "Parent category ID is required")
    private Long parentCategoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private Set<Long> variationIds = new HashSet<>();

    private Boolean isActive;

    private Instant createdAt;

}
