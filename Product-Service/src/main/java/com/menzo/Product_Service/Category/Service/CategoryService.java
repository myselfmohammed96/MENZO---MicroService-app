package com.menzo.Product_Service.Category.Service;

import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Repo.CategoriesRepo;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Exception.DuplicateCategoryException;
import com.menzo.Product_Service.Category.Dto.CreateParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.CreateSubCategoryDto;
import com.menzo.Product_Service.Category.Dto.ParentCategoryDto;
import com.menzo.Product_Service.Category.Dto.SubCategoryDto;
import com.menzo.Product_Service.Variation.Repo.VariationsRepository;
import com.menzo.Product_Service.GlobalComponents.Service.UtilityService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Autowired
    private VariationsRepository variationsRepo;

    @Autowired
    private UtilityService utilityService;



//    ********* Parent categories *********

    //    Add new parent category - TESTED
    public ProductCategory addNewParentCategory(CreateParentCategoryDto newParentCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryName(newParentCategory.getCategoryName())) {
            logger.error("Parent category '{}' already exists", newParentCategory.getCategoryName());
            throw new DuplicateCategoryException("Category already exists.");
        }

        // saving new Parent category
        ProductCategory newProductCategory = ProductCategory.builder()
                .categoryName(newParentCategory.getCategoryName())
                .abbreviation(null)
                .parentCategoryId(null)
                .isActive(true)
                .isDeleted(false)
                .build();
        logger.info("Saving new parent category: {}", newParentCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }



    //    Update parent category by ID - TESTED
    public ProductCategory updateParentCategory(Long parentCategoryId, ParentCategoryDto latestParentCategory) {

        //  fetching parent category by ID
        ProductCategory parentCategory = categoriesRepo.findByIdAndParentCategoryIdIsNull(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));

        //  updating newly available fields
        parentCategory.setCategoryName(
                latestParentCategory.getCategoryName() != null
                        && !latestParentCategory.getCategoryName().isEmpty()
                        ? latestParentCategory.getCategoryName()
                        : parentCategory.getCategoryName()
        );
        parentCategory.setIsActive(
                latestParentCategory.getIsActive() != null
                        ? latestParentCategory.getIsActive()
                        : parentCategory.getIsActive()
        );
        logger.info("Updated parent category with ID: {}", parentCategoryId);
        return categoriesRepo.save(parentCategory);
    }

    /*
     *  Soft delete parent category by ID
     *
     *  With 'cascade Delete' option - Deleting all the sub-categories with Soft delete
     */
    public boolean deleteParentCategory(Long parentCategoryId) {

        //  fetching parent category by ID
        ProductCategory parentCategory = categoriesRepo.findByIdAndParentCategoryIdIsNull(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));

        //  delete check validation
        if (parentCategory.getIsDeleted()) throw new RuntimeException("Parent category with ID (" + parentCategoryId + ") already deleted");

        // soft delete: set isDelete to true if not already
        logger.info("Deleting parent category with ID: {}", parentCategoryId);
        parentCategory.setIsDeleted(true);
        ProductCategory softDeletedParent = categoriesRepo.save(parentCategory);
        return softDeletedParent.getIsDeleted();
    }



//    ********* Sub categories *********

    //    Add new sub category - TESTED
    //  ## Pending: Color & size variations - as default for all
    public ProductCategory addNewSub(CreateSubCategoryDto newSubCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryNameAndParentCategoryId(
                newSubCategory.getCategoryName(),
                newSubCategory.getParentCategoryId()
        )) {
            logger.error("Sub-category '{}' already exists under parent Id {}", newSubCategory.getCategoryName(), newSubCategory.getParentCategoryId());
            throw new DuplicateCategoryException("Category already exists under this parent.");
        }

        //  building variations set
        //  ## pending - provide crud on variation set of the sub-category
        List<Variation> variationsList = variationsRepo.findAllById(newSubCategory.getVariationIds());
        Set<Variation> variations = new HashSet<>(variationsList);

        //  generating abbreviation for the new sub-category name
        String abb = utilityService.generateAbbreviation(
                "sub-category",
                newSubCategory.getCategoryName()
        );

        //  Save new sub-category
        ProductCategory newProductCategory = ProductCategory.builder()
                .parentCategoryId(newSubCategory.getParentCategoryId())
                .categoryName(newSubCategory.getCategoryName())
                .abbreviation(abb)
                .variations(variations)
                .isActive(true)
                .isDeleted(false)
                .build();
        logger.info("Saving new sub-category under parent ID {}: {}", newSubCategory.getParentCategoryId(), newSubCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }

    //    Update sub category by ID - TESTED
    public ProductCategory updateSubCategory(Long subCategoryId, SubCategoryDto latestSubCategory) {

        //  fetching sub-category by ID
        ProductCategory subCategory = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));

        // updating the available fields in latestSubCategory
        logger.info("Updating sub-category with ID {}", subCategoryId);
        subCategory.setParentCategoryId(
                latestSubCategory.getParentCategoryId() != null
                        && latestSubCategory.getParentCategoryId() > 0
                        ? latestSubCategory.getParentCategoryId()
                        : subCategory.getParentCategoryId()
        );
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
        subCategory.setIsActive(
                latestSubCategory.getIsActive() != null
                        ? latestSubCategory.getIsActive()
                        : subCategory.getIsActive()
        );
        logger.info("Updated sub category with ID: {}", subCategoryId);
        return categoriesRepo.save(subCategory);
    }

    //    Soft delete sub category by ID - TESTED
    public boolean deleteSubCategory(Long subCategoryId) {

        //  fetching sub-Category by ID
        ProductCategory subCategory = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));

        //  delete check validation
        if (subCategory.getIsDeleted()) throw new RuntimeException("Sub-category with ID (" + subCategoryId + ") already deleted");

        //  soft delete: set isDelete to true if not already
        logger.info("Deleting sub-category with ID {}", subCategoryId);
        subCategory.setIsDeleted(true);
        ProductCategory softDeletedSub = categoriesRepo.save(subCategory);
        return softDeletedSub.getIsDeleted();
    }

}
