package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.*;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Exception.DuplicateCategoryException;
import com.menzo.Product_Service.Repository.CategoriesRepo;
import com.menzo.Product_Service.Repository.VariationsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoriesService {

    private static final Logger log = LoggerFactory.getLogger(CategoriesService.class);

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private UtilityService utilityService;



//    ********* Parent categories *********

    //    Add new parent category - TESTED
    public ProductCategory addNewParentCategory(CreateParentCategoryDto newParentCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryName(newParentCategory.getCategoryName())) {
            log.error("Parent category '{}' already exists", newParentCategory.getCategoryName());
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
        log.info("Saving new parent category: {}", newParentCategory.getCategoryName());
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
        log.info("Updated parent category with ID: {}", parentCategoryId);
        return categoriesRepo.save(parentCategory);
    }

    /*
     *  Delete parent category by ID - Soft Delete
     *
     *  With 'cascade Delete' option - Deleting all the sub-categories with Soft delete
     */
    public boolean deleteParentCategory(Long parentCategoryId) {

        //  fetching parent category by ID
        ProductCategory parentCategory = categoriesRepo.findByIdAndParentCategoryIdIsNull(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));

        //  delete check validation
        if (parentCategory.getIsDeleted()) {
            throw new RuntimeException("Parent category with ID (" + parentCategoryId + ") already deleted");
        }

        // Soft deleting parent category by ID
        parentCategory.setIsDeleted(!parentCategory.getIsDeleted() ? true : parentCategory.getIsDeleted());
        ProductCategory isDeleteUpdatedParent = categoriesRepo.save(parentCategory);
        return isDeleteUpdatedParent.getIsDeleted();
    }



//    ********* Sub categories *********

    //    Add new sub category - TESTED
    //  ## Pending: Color & size variations - as default for all
    @Transactional
    public ProductCategory addNewSub(CreateSubCategoryDto newSubCategory) {

        //  duplicate - existence validation
        if (categoriesRepo.existsByCategoryNameAndParentCategoryId(
                newSubCategory.getCategoryName(),
                newSubCategory.getParentCategoryId()
        )) {
            log.error("Sub-category '{}' already exists under parent Id {}", newSubCategory.getCategoryName(), newSubCategory.getParentCategoryId());
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
        log.info("Saving new sub-category under parent ID {}: {}", newSubCategory.getParentCategoryId(), newSubCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }

    //    Update sub category by id - TESTED
    public ProductCategory updateSubCategory(Long subCategoryId, SubCategoryDto latestSubCategory) {

        //  fetching the sub-category by ID
        ProductCategory subCategory = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));

        // updating the available fields in latestSubCategory
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
        log.info("Updated sub category with ID: {}", subCategoryId);
        return categoriesRepo.save(subCategory);
    }

    //    Delete sub category by id
//    public boolean deleteSubCategory(Long subCategoryId) {
//        boolean subCategoryExists = categoriesRepo.existsById(subCategoryId);
//        if (!subCategoryExists) {
//            log.error("Sub-category not found with ID {}", subCategoryId);
//            throw new EntityNotFoundException("Sub category not found with ID: " + subCategoryId);
//        }
//        log.info("Deleting sub-category with ID {}", subCategoryId);
//        categoriesRepo.deleteSubById(subCategoryId);
//        boolean exists = categoriesRepo.existsById(subCategoryId);
//        if (!exists) {
//            log.info("Sub-category with ID {} successfully deleted", subCategoryId);
//            return true;
//        } else {
//            log.error("Sub-category with ID {} could not be deleted", subCategoryId);
//            return false;
//        }
//    }

}
