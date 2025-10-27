package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Dto.CategoriesDto.*;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Service.CategoriesRetrievalService;
import com.menzo.Product_Service.Service.CategoriesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoriesRestController {

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    private static final Logger logger = LoggerFactory.getLogger(CategoriesRestController.class);

//    ********* GET - Controllers *********
//    ********* Parent-categories *********

    //  Get all parent categories - without sub-categories (id, categoryName, isActive, createdAt)
    //  TESTED - with Postman
    @GetMapping("get-all-parents")
    public List<ParentCategoryDto> getAllParents() {
        return categoriesRetrievalService.getAllParents();
    }

    //  Get all parent categories - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName))
    //  TESTED - with Postman
    @GetMapping("/get-all")
    public List<NestedCategoryDto> getAllParentCategories() {
        return categoriesRetrievalService.getAllParentWithSub();
    }

    //  Get parent category by id - without sub-categories (id, categoryName, isActive, createdAt)
    //  TESTED - with Postman
    @GetMapping("/get-parent")
    public ResponseEntity<?> getParentCategoryById(@RequestParam("id") Long parentCategoryId) {
        if (parentCategoryId == null || parentCategoryId <= 0) {
            logger.warn("Invalid parent category ID: {}", parentCategoryId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
        }
        ParentCategoryDto parentCategoryDto = categoriesRetrievalService.getParentCategoryById(parentCategoryId);
        return ResponseEntity.ok(parentCategoryDto);
    }

    //  Get parent category by id - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName)) --- @RequestHeader("roles") String roles,
    //  TESTED - with Postman
    @GetMapping("get")
    public ResponseEntity<?> getParentCategoryByIdWithSub(@RequestParam("id") Long parentCategoryId) {
        if (parentCategoryId == null || parentCategoryId <= 0) {
            logger.warn("Invalid parent category ID: {}", parentCategoryId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
        }
        NestedCategoryDto parentCategoryWithSub = categoriesRetrievalService.getParentCategoryByIdWithSub(parentCategoryId);
        return ResponseEntity.ok(parentCategoryWithSub);
    }



//    ********* Sub-categories *********

    //  Get all sub category by parent id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    //  TESTED - with Postman
    @GetMapping("get-all-sub")
    public ResponseEntity<?> getAllSubCategoriesByParentId(@RequestHeader("roles") String roles,
                                                           @RequestParam("id") Long parentId) {
        if (roles.equals("ADMIN")) {
            if (parentId == null || parentId <= 0) {
                logger.warn("Invalid parent ID: {}", parentId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent ID"));
            }
            List<SubCategoryDto> allSubOfParentId = categoriesRetrievalService.getAllSubOfParentId(parentId);
            return ResponseEntity.ok(allSubOfParentId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //  Get sub category by id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    //  TESTED - with Postman
    @GetMapping("/get-sub")
    public ResponseEntity<?> getSubCategoryById(@RequestHeader("roles") String roles,
                                                @RequestParam("id") Long subCategoryId) {
        if (roles.equals("ADMIN")) {
            if (subCategoryId == null || subCategoryId <= 0) {
                logger.warn("Invalid sub-category ID: {}", subCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid sub-category ID"));
            }
            SubCategoryDto subCategoryDto = categoriesRetrievalService.getSubCategoryById(subCategoryId);
            return ResponseEntity.ok(subCategoryDto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/get-sub-by-product-id")
//    public ResponseEntity<ProductCategory> getSubCategoryByProductId(@RequestParam("id") Long productId) {
//        ProductCategory subCategory = categoriesRetrievalService.getSubByProductId(productId);
//        return ResponseEntity.ok(subCategory);
//    }



///    ********* POST, PUT, DELETE - Controllers *********
//    ********* Parent-Categories *********

    //    Add new parent category
    @PostMapping("/add-parent")
    public ResponseEntity<?> addParentCategory(@RequestHeader("roles") String roles,
                                               @Valid @RequestBody CreateParentCategoryDto newParentCategory,
                                               BindingResult result) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage()));
                logger.warn("Validation failed for new parent category: {}", errors);
                return ResponseEntity.badRequest().body(errors);
            }

            //  adding parent category
            ProductCategory savedCategory = categoriesService.addNewParentCategory(newParentCategory);

            //  response
            if (savedCategory != null) {
                logger.info("Parent category created successfully with ID: {}", savedCategory.getId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Parent category created successfully", "categoryId", savedCategory.getId()));
            } else {
                logger.error("Parent category creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Parent category creation failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //    Update parent category by id
    @PutMapping("/update-parent")
    public ResponseEntity<?> updateParentCategory(@RequestHeader("roles") String roles,
                                                  @RequestParam("id") Long parentCategoryId,
                                                  @RequestBody ParentCategoryDto latestParentCategory) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (parentCategoryId == null || parentCategoryId <= 0) {
                logger.warn("Invalid parent category ID: {}", parentCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
            }

            //  updating parent category
            ProductCategory updatedParentCategory = categoriesService.updateParentCategory(parentCategoryId, latestParentCategory);

            //  response
            if (updatedParentCategory != null) {
                logger.info("Parent category with ID {} updated successfully", parentCategoryId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Parent category updated successfully", "categoryId", updatedParentCategory.getId()));
            } else {
                logger.error("Parent update failed for ID {}", parentCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Parent category updation failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //    Delete parent category by id
    @DeleteMapping("/delete-parent")
    public ResponseEntity<?> deleteParentCategory(@RequestHeader("roles") String roles,
                                                  @RequestParam("id") Long parentCategoryId) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (parentCategoryId == null || parentCategoryId <= 0) {
                logger.warn("Invalid parent category ID: {}", parentCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid parent category ID"));
            }

            //  deleting parent category
            boolean deleted = categoriesService.deleteParentCategory(parentCategoryId);

            //  response
            if (deleted) {
                logger.info("Parent category with ID {} deleted successfully", parentCategoryId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Parent category deleted successfully"));
            } else {
                logger.error("Parent category deletion failed for ID {}", parentCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Parent category deletion failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }



//    ********* Sub-categories *********

    //    Add new sub category
    @PostMapping("/add-sub")
    public ResponseEntity<?> addSubCategory(@RequestHeader("roles") String roles,
                                            @Valid @RequestBody CreateSubCategoryDto newSubCategory,
                                            BindingResult result) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage()));
                logger.warn("Validation failed for new sub-category: {}", errors);
                return ResponseEntity.badRequest().body(errors);
            }

            //  adding sub-category
            ProductCategory savedCategory = categoriesService.addNewSub(newSubCategory);

            //  response
            if (savedCategory != null) {
                logger.info("Sub-category created successfully with ID: {}", savedCategory.getId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Sub-category created successfully", "categoryId", savedCategory.getId()));
            } else {
                logger.error("Sub-category creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Sub-category creation failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //    Update sub category by ID
    @PutMapping("/update-sub")
    public ResponseEntity<?> updateSubCategory(@RequestHeader("roles") String roles, @RequestParam("id") Long subCategoryId, @RequestBody SubCategoryDto latestSubCategory) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (subCategoryId == null || subCategoryId <= 0) {
                logger.warn("Invalid sub-category ID: {}", subCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid sub-category ID"));
            }

            //  updating sub-category
            ProductCategory updatedSubCategory = categoriesService.updateSubCategory(subCategoryId, latestSubCategory);

            //  response
            if (updatedSubCategory != null) {
                logger.info("Sub-category with ID {} updated successfully", subCategoryId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Sub-category updated successfully", "categoryId", updatedSubCategory.getId()));
            } else {
                logger.error("Sub-category update failed for ID {}", subCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Sub-category updation failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //    Delete sub category by ID
    @DeleteMapping("/delete-sub")
    public ResponseEntity<?> deleteSubCategory(@RequestHeader("roles") String roles,
                                               @RequestParam("id") Long subCategoryId) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (subCategoryId == null || subCategoryId <= 0) {
                logger.warn("Invalid sub-category ID: {}", subCategoryId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid sub-category ID"));
            }

            //  deleting sub-category
            boolean deleted = categoriesService.deleteSubCategory(subCategoryId);

            //  response
            if (deleted) {
                logger.info("Sub-category with ID {} deleted successfully", subCategoryId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Sub-category deleted successfully"));
            } else {
                logger.error("Sub-category deletion failed for ID {}", subCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Sub-category deletion failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}









//    Get all parent categories with Banner images - without sub-categories (id, categoryName, categoryBannerImg)
//    @GetMapping("/get-all-with-banner")
//    public List<CategoryMinimalDto> getAllCategoriesWithBanner() {
//        return categoriesRetrievalService.getAllCategoriesWithBanner();
//    }









//    Get all sub-categories by parent id with Banner images - (id, categoryName, categoryBannerImg)
//    @GetMapping("/get-sub-with-banner")
//    public ResponseEntity<List<CategoryMinimalDto>> getAllSubCategoriesWithBanner(@RequestParam("id") Long parentId) {
//        List<CategoryMinimalDto> subCategoriesList = categoriesRetrievalService
//                .getAllSubCategoriesByParentIdWithBanner(parentId);
//        return ResponseEntity.ok(subCategoriesList);
//    }