package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.CreateParentCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoriesServiceTest {

    @Autowired
    private CategoriesService categoriesService;

    @Test
    public void testAddNewParent() {
        CreateParentCategoryDto newParent = CreateParentCategoryDto.builder()
                .categoryName("Two More")
                .isActive(true)
                .build();
        ProductCategory parent = categoriesService.addNewParent(newParent);
        System.out.println(parent);
    }

    @Test
    public void testUpdateParentCategory() {
        ParentCategoryDto parentDto = ParentCategoryDto.builder()
                .categoryName("Thirty more")
                .build();
        ProductCategory updated = categoriesService.updateParentCategory(159L, parentDto);
        System.out.println(updated);
    }
}