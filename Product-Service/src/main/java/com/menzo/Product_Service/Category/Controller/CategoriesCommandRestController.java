package com.menzo.Product_Service.Category.Controller;

import com.menzo.Product_Service.Category.Dto.*;
import com.menzo.Product_Service.Category.Service.CategoryQueryService;
import com.menzo.Product_Service.Category.Service.CategoryCommandService;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
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
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoriesCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriesCommandRestController.class);

    @Autowired
    private CategoryCommandService categoryCommandService;

    @Autowired
    private CategoryQueryService categoriesRetrievalService;


//    ********* Parent-Categories *********


    /*
     *
     *   Add new parent category
     *
     */
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
                logger.info("Parent category created successfully with ID: {}", savedCategory.getCategoryId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Parent category created successfully", "categoryId", savedCategory.getCategoryId()));
            } else {
                logger.error("Parent category creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Parent category creation failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update parent category
     *   Parent category identified by category ID
     *
     */
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
                        .body(Map.of("message", "Parent category updated successfully", "categoryId", updatedParentCategory.getCategoryId()));
            } else {
                logger.error("Parent update failed for ID {}", parentCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Parent category update failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update parent category active status
     *
     */
    @PutMapping("/update-parent-status")
    public ResponseEntity<?> updateParentActiveStatus() {
    }


    /*
     *
     *   Delete parent category
     *   Parent category identified by category ID
     *
     */
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


    /*
     *
     *   Add new sub-category
     *
     */
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
                logger.info("Sub-category created successfully with ID: {}", savedCategory.getCategoryId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Sub-category created successfully", "categoryId", savedCategory.getCategoryId()));
            } else {
                logger.error("Sub-category creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Sub-category creation failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update sub-category
     *   Sub-category identified by category ID
     *
     */
    @PutMapping("/update-sub")
    public ResponseEntity<?> updateSubCategory(@RequestHeader("roles") String roles,
                                               @RequestParam("id") Long subCategoryId,
                                               @RequestBody SubCategoryDto latestSubCategory) {
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
                        .body(Map.of("message", "Sub-category updated successfully", "categoryId", updatedSubCategory.getCategoryId()));
            } else {
                logger.error("Sub-category update failed for ID {}", subCategoryId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Sub-category updation failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update sub-category active status
     *
     */
    @PutMapping("/update-sub-status")
    public ResponseEntity<?> updateSubActiveStatus() {
    }


    /*
     *
     *   Delete sub-category
     *   Sub-category identified by category ID
     *
     */
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



















