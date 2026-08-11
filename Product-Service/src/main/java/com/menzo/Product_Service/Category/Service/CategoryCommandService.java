package com.menzo.Product_Service.Category.Service;

import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Repository.CategoriesRepository;
import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableCategoryFilter;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Exception.DuplicateCategoryException;
import com.menzo.Product_Service.Category.Dto.CreateParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.CreateSubCategoryDto;
import com.menzo.Product_Service.Category.Dto.CategoryDto;
import com.menzo.Product_Service.GlobalComponents.Service.UtilityService;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryCommandService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryCommandService.class);

    @Autowired
    private CategoriesRepository categoriesRepo;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private VariationQueryService variationQueryService;

    @Autowired
    private UtilityService utilityService;


//    ********* Parent categories *********


    /*
     *
     *   Add new parent category
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory addNewParentCategory(CreateParentCategoryDto newParentCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryName(newParentCategory.getCategoryName())) {
            logger.error("Parent category '{}' already exists", newParentCategory.getCategoryName());
            throw new DuplicateCategoryException("Category already exists.");
        }

        // saving new Parent category
        ProductCategory newProductCategory = ProductCategory.builder()
                .parentCategory(null)
                .categoryName(newParentCategory.getCategoryName())
                .abbreviation(null)
                .build();
        logger.info("Saving new parent category: {}", newParentCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }


    /*
     *
     *   Update parent category
     *   Parent category identified by category ID
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory updateParentCategory(UUID parentCategoryId,
                                                CategoryDto latestParentCategory) {

        //  fetching parent category by ID
        ProductCategory parentCategory = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNull(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));

        //  updating newly available fields
        parentCategory.setCategoryName(
                latestParentCategory.getCategoryName() != null
                        && !latestParentCategory.getCategoryName().isEmpty()
                        ? latestParentCategory.getCategoryName()
                        : parentCategory.getCategoryName()
        );
        logger.info("Updated parent category with ID: {}", parentCategoryId);
        return categoriesRepo.save(parentCategory);
    }


//    ********* Sub categories *********


    /*
     *
     *   Add new sub-category
     *   ## Pending: color & size variations - as default for all
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory addNewSubCategory(CreateSubCategoryDto newSubCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryNameAndParentCategory_CategoryId(
                newSubCategory.getCategoryName(),
                newSubCategory.getParentCategoryId()
        )) {
            logger.error("Sub-category '{}' already exists under parent Id {}", newSubCategory.getCategoryName(), newSubCategory.getParentCategoryId());
            throw new DuplicateCategoryException("Category already exists under this parent.");
        }

        //  fetch parent category
        ProductCategory parent = categoryQueryService.getParentCategoryEntityById(newSubCategory.getParentCategoryId());

        //  building variations set
        //  ## pending - provide crud on variation set of the sub-category
        Set<Variation> variations = variationQueryService.getVariationSetByIds(newSubCategory.getVariationIds());

        //  generating abbreviation for the new sub-category name
        String abb = utilityService.generateAbbreviation(
                "sub-category",
                newSubCategory.getCategoryName()
        );

        //  Save new sub-category
        ProductCategory newProductCategory = ProductCategory.builder()
                .parentCategory(parent)
                .categoryName(newSubCategory.getCategoryName())
                .abbreviation(abb)
                .variations(variations)
                .build();
        logger.info("Saving new sub-category under parent ID {}: {}", newSubCategory.getParentCategoryId(), newSubCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }


    /*
     *
     *   Update sub-category
     *   Sub-category identified by category ID
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory updateSubCategory(UUID subCategoryId, CategoryDto latestSubCategory) {

        //  fetching sub-category by ID
        ProductCategory subCategory = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));

        // updating the available fields in latestSubCategory
        subCategory.setCategoryName(
                latestSubCategory.getCategoryName() != null
                        && !latestSubCategory.getCategoryName().isEmpty()
                        ? latestSubCategory.getCategoryName()
                        : subCategory.getCategoryName()
        );
        subCategory.setAbbreviation(
                latestSubCategory.getCategoryName() != null
                        && !latestSubCategory.getCategoryName().isEmpty()
                        ? utilityService.generateAbbreviation("sub-category", latestSubCategory.getCategoryName())
                        : subCategory.getAbbreviation()
        );
        logger.info("Updated sub category with ID: {}", subCategoryId);
        return categoriesRepo.save(subCategory);
    }


    //    ********* Common methods - parent & sub categories *********


    /*
     *
     *   Update category active status
     *   Category identified by category ID
     *   Category level differentiated by Components - CATEGORY or SUB_CATEGORY
     *
     */
    @Transactional
    @EnableCategoryFilter
    public boolean updateCategoryActiveStatus(UUID categoryId,
                                              boolean isActive,
                                              Components categoryLevel) {
        if (categoryLevel == Components.CATEGORY) {
            ProductCategory parent = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNull(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + categoryId));
            parent.setActive(isActive);
            return categoriesRepo.save(parent).isActive();
        } else if (categoryLevel == Components.SUB_CATEGORY) {
            ProductCategory sub = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNotNull(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Sub-category not found with ID: " + categoryId));
            sub.setActive(isActive);
            return categoriesRepo.save(sub).isActive();
        } else {
            throw new IllegalArgumentException("Component of impact should be either parent category or sub-category.");
        }
    }


    /*
     *
     *   Delete category (soft delete)
     *   Category identified by category ID
     *   Category level differentiated by Components - CATEGORY or SUB_CATEGORY
     *   ## With 'cascade Delete' option - Deleting all the sub-categories with Soft delete
     *
     */
    @Transactional
    @EnableCategoryFilter
    public boolean deleteCategory(UUID categoryId,
                                  Components categoryLevel) {
        //  fetching category
        ProductCategory category;
        if (categoryLevel == Components.CATEGORY) {
            //  parent category
            category = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNull(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found with category ID: " + categoryId));
        } else if (categoryLevel == Components.SUB_CATEGORY) {
            //  sub-category
            category = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNotNull(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Sub-category not found with category ID: " + categoryId));
        } else {
            throw new IllegalArgumentException("Component of impact should be either parent category or sub-category");
        }

        // soft delete: set isDelete to true if not already
        logger.info("Deleting category with ID: {}", categoryId);
        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        categoriesRepo.save(category);
        return true;
    }

}
