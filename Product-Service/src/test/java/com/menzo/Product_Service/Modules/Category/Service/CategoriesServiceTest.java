package com.menzo.Product_Service.Modules.Category.Service;

import com.menzo.Product_Service.Category.Dto.CreateParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.CreateSubCategoryDto;
import com.menzo.Product_Service.Category.Dto.ParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.SubCategoryDto;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootTest
class CategoriesServiceTest {

    @Autowired
    private CategoryService categoriesService;



    //    ******* Parent categories methods *******
    @Test
    public void testAddNewParentCategory() {
        CreateParentCategoryDto newParent = CreateParentCategoryDto.builder()
                .categoryName("Three More")
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
        boolean deleted = categoriesService.deleteParentCategory(165L);
        System.out.println("Deleted: " + deleted);
    }



//    ******* Sub-categories methods *******

    @Test
    public void testAddNewSub() {
        CreateSubCategoryDto newSub = CreateSubCategoryDto.builder()
                .parentCategoryId(159L)
                .categoryName("Test Sub 22")
                .variationIds(new HashSet<>(Arrays.asList(3L, 4L, 9L)))
                .build();
        ProductCategory savedSub = categoriesService.addNewSub(newSub);
        System.out.println("Sub: " + savedSub);
    }

    @Test
    public void testUpdateSubCategory() {
        SubCategoryDto latestData = SubCategoryDto.builder()
                .categoryName("Test sub category")
                .isActive(false)
                .build();
        ProductCategory updated = categoriesService.updateSubCategory(
                160L,
                latestData
        );
        System.out.println("Updated: " + updated);
    }

    @Test
    public void testDeleteSubCategory() {
        boolean deleted = categoriesService.deleteSubCategory(163L);
        System.out.println("Deleted: " + deleted);
    }
}