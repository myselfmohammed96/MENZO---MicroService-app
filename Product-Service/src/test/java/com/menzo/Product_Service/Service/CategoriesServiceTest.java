package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.CreateParentCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.CreateSubCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoriesServiceTest {

    @Autowired
    private CategoriesService categoriesService;

    @Test
    public void testAddNewParentCategory() {
        CreateParentCategoryDto newParent = CreateParentCategoryDto.builder()
                .categoryName("Two More")
                .isActive(true)
                .build();
        ProductCategory parent = categoriesService.addNewParentCategory(newParent);
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

    @Test
    public void testDeleteParentCategory() {
        boolean deleted = categoriesService.deleteParentCategory(159L);
        System.out.println("Deleted: " + deleted);
    }

    @Test
    @Transactional
    public void testAddNewSub() {
        CreateSubCategoryDto newSub = CreateSubCategoryDto.builder()
                .parentCategoryId(159L)
                .categoryName("Test Sub")
                .variationIds(new HashSet<>(Arrays.asList(3L, 4L, 9L)))
                .build();
        Object s = "he";
        s.equals()
        ProductCategory savedSub = categoriesService.addNewSub(newSub);
        System.out.println("Sub: " + savedSub);
    }
}