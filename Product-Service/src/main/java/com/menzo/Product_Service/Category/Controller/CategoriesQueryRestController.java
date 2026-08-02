package com.menzo.Product_Service.Category.Controller;

import com.menzo.Product_Service.Category.Dto.NestedCategoryDto;
import com.menzo.Product_Service.Category.Dto.ParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.SubCategoryDto;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Service.CategoryQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoriesQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriesQueryRestController.class);

    @Autowired
    private CategoryQueryService categoryQueryService;


//    ********* Parent-categories *********


    /*
     *
     *   Get all parent categories
     *   Without sub-categories
     *
     */
    @GetMapping("get-all-parents")
    public ResponseEntity<List<ParentCategoryDto>> getAllParents() {
        List<ParentCategoryDto> parents = categoryQueryService.getAllParents();
        return ResponseEntity.ok(parents);
    }


    /*
     *
     *   Get all parent categories
     *   With sub-categories
     *
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<NestedCategoryDto>> getAllParentCategories() {
        List<NestedCategoryDto> parents = categoryQueryService.getAllParentWithSub();
        return ResponseEntity.ok(parents);
    }


    /*
     *
     *   Get parent category
     *   Without sub-categories
     *   Parent category identified by parent category ID
     *
     */
    @GetMapping("/get-parent")
    public ResponseEntity<?> getParentCategoryById(@RequestParam("id") Long parentCategoryId) {
        //  validation
        if (parentCategoryId == null || parentCategoryId <= 0) {
            logger.warn("Invalid parent category ID: {}", parentCategoryId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
        }
        ParentCategoryDto parentCategoryDto = categoryQueryService.getParentCategoryById(parentCategoryId);
        return ResponseEntity.ok(parentCategoryDto);
    }


    /*
     *
     *   Get parent category
     *   With sub-categories
     *   Parent category identified by parent category ID
     *
     */
    @GetMapping("get")
    public ResponseEntity<?> getParentCategoryByIdWithSub(@RequestParam("id") Long parentCategoryId) {
        if (parentCategoryId == null || parentCategoryId <= 0) {
            logger.warn("Invalid parent category ID: {}", parentCategoryId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
        }
        NestedCategoryDto parentCategoryWithSub = categoryQueryService.getParentCategoryByIdWithSub(parentCategoryId);
        return ResponseEntity.ok(parentCategoryWithSub);
    }


    /*
     *
     *   Get all parent categories with banner images
     *   Without sub-categories
     *
     */
//    @GetMapping("/get-all-with-banner")
//    public ResponseEntity<List<CategoryMinimalDto>> getAllCategoriesWithBanner() {
//        List<CategoryMinimalDto> categories = categoryQueryService.getAllCategoriesWithBanner();
//        return ResponseEntity.ok(categories);
//    }


//    ********* Sub-categories *********


    /*
     *
     *   Get all sub-categories
     *   Without variations
     *   Sub-categories identified by parent category ID
     *
     */
    @GetMapping("get-all-sub")
    public ResponseEntity<?> getAllSubCategoriesByParentId(@RequestHeader("roles") String roles,
                                                           @RequestParam("id") Long parentId) {
        if (roles.equals("ADMIN")) {
            if (parentId == null || parentId <= 0) {
                logger.warn("Invalid parent ID: {}", parentId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent ID"));
            }
            List<SubCategoryDto> allSubOfParentId = categoryQueryService.getAllSubCategoriesByParentId(parentId);
            return ResponseEntity.ok(allSubOfParentId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Get sub-category
     *   Without variations
     *   Sub-category identified by sub-category ID
     *
     */
    @GetMapping("/get-sub")
    public ResponseEntity<?> getSubCategoryById(@RequestHeader("roles") String roles,
                                                @RequestParam("id") Long subCategoryId) {
        if (roles.equals("ADMIN")) {
            if (subCategoryId == null || subCategoryId <= 0) {
                logger.warn("Invalid sub-category ID: {}", subCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid sub-category ID"));
            }
            SubCategoryDto subCategory = categoryQueryService.getSubCategoryById(subCategoryId);
            return ResponseEntity.ok(subCategory);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Get sub-category
     *   Sub-category identified by product ID
     *
     */
//    @GetMapping("/get-sub-by-product-id")
//    public ResponseEntity<ProductCategory> getSubCategoryByProductId(@RequestParam("id") Long productId) {
//        ProductCategory subCategory = categoryQueryService.getSubByProductId(productId);
//        return ResponseEntity.ok(subCategory);
//    }


    /*
     *
     *   Get all sub-categories with banner images
     *   Sub-categories identified by parent category ID
     *
     */
//    @GetMapping("/get-sub-with-banner")
//    public ResponseEntity<List<CategoryMinimalDto>> getAllSubCategoriesWithBanner(@RequestParam("id") Long parentId) {
//        List<CategoryMinimalDto> subCategoriesList = categoryQueryService
//                .getAllSubCategoriesByParentIdWithBanner(parentId);
//        return ResponseEntity.ok(subCategoriesList);
//    }

}
