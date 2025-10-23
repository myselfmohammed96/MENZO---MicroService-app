package com.menzo.Product_Service.Dto.CategoriesDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateParentCategoryDto {

    private Long id;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private Boolean isActive;

    private Instant createdAt;

}
