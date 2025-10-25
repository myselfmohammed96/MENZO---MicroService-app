package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepo extends JpaRepository<ProductCategory, Long> {

    /*
    * ********* existence check *********
    */

    public boolean existsByCategoryName(String categoryName);       //  TESTED

    public boolean existsByAbbreviation(String abbreviation);       //  TESTED

    public boolean existsByCategoryNameAndParentCategoryId(String categoryName, Long parentCategoryId);     //  TESTED


    /*
    *  ********* find methods *********
    *  ********* Parent categories *********
    */

    //  find all parent categories
    public List<ProductCategory> findByParentCategoryIdIsNull();        //  TESTED

    //  find parent category by ID
    public Optional<ProductCategory> findByIdAndParentCategoryIdIsNull(Long id);        //  TESTED

    //  find all parent categories with their sub-categories list
    @Query(
            nativeQuery = true,
            value =
                    "SELECT parent.id AS parentId, parent.category_name AS parentName, " +
                            "child.id AS subId, child.category_name AS subName " +
                            "FROM product_categories parent " +
                            "LEFT JOIN product_categories child " +
                            "ON child.parent_category_id = parent.id " +
                            "WHERE parent.parent_category_id IS NULL " +
                            "ORDER BY parent.id, child.id"
    )
    public List<Object[]> findAllParentWithSub();                                   // TESTED

    //  find parent category by ID with its sub-categories
    @Query(
            nativeQuery = true,
            value =
                    "SELECT parent.id AS parentId, parent.category_name AS parentName, " +
                            "child.id AS subId, child.category_name AS subName " +
                            "FROM product_categories parent " +
                            "LEFT JOIN product_categories child " +
                            "ON child.parent_category_id = parent.id " +
                            "WHERE parent.parent_category_id IS NULL " +
                            "AND parent.id = :parentCategoryId " +
                            "ORDER BY parent.id, child.id"
    )
    public List<Object[]> findParentByIdWithSub(@Param("parentCategoryId") Long parentCategoryId);  // TESTED

    //  find parent category by given sub-category id
    @Query(
            value =
                    "SELECT c1.id AS id, c1.category_name AS categoryName " +
                            "FROM product_categories c1 " +
                            "WHERE c1.is_active = 1 AND id = (" +
                            "SELECT c2.parent_category_id " +
                            "FROM product_categories c2 " +
                            "WHERE c2.is_active = 1 " +
                            "AND c2.parent_category_id IS NOT NULL " +
                            "AND c2.id = :subCategoryId" +
                            ")",
            nativeQuery = true
    )
    public ParentCategoryView findParentCategoryBySubId(Long subCategoryId);        //  TESTED



    /*
    *   ********* Sub-categories *********
    */

    //  find sub-category by ID
    public Optional<ProductCategory> findByIdAndParentCategoryIdIsNotNull(Long id);         // TESTED

    //  find all sub-categories by parent category ID
    public List<ProductCategory> findAllByParentCategoryId(Long parentCategoryId);          // TESTED



    /*
    *
    * probably unnecessary
    * can be done with findByIdAndParentCategoryIdIsNotNull() - and transferring to dto in the business
    * try with post man
    *
     */
//    @Query(
//            "SELECT new com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto(" +
//                    "pc.id, pc.parentCategoryId, pc.categoryName, pc.isActive, pc.createdAt) " +
//                    "FROM ProductCategory pc " +
//                    "WHERE pc.parentCategoryId IS NOT NULL " +
//                    "AND pc.id = :subCategoryId"
//    )
//    public SubCategoryDto findSubByIdWithoutVariation(@Param("subCategoryId") Long subCategoryId);

}








//    @Query("SELECT new com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto(pc.id, pc.parentCategoryId, pc.categoryName, " +
//            "pc.isActive, pc.createdAt) FROM ProductCategory pc WHERE pc.parentCategoryId = :parentId")
//    public List<SubCategoryDto> findAllSubByParentId(@Param("parentId") Long parentId);







///*
// *   ********* Delete methods *********
// *
// *   Includes for both parent-category & sub-category
// */
//
//    @Query(
//            nativeQuery = true,
//            value = "DELETE FROM product_categories " +
//            "WHERE parent_category_id IS NULL " +
//            "AND id = :parentCategoryId"
//    )
//    public void deleteParentById(@Param("parentCategoryId") Long parentCategoryId);
//
//    @Query(
//            nativeQuery = true,
//            value = "DELETE FROM product_categories " +
//                    "WHERE parent_category_id IS NOT NULL " +
//                    "AND id = :subCategoryId"
//    )
//    public void deleteSubById(@Param("subCategoryId") Long subCategoryId);





//    @Query(nativeQuery = true, value = "SELECT pc.id, pc.parent_category_id, pc.category_name, pc.is_active, pc.created_at " +
//            "FROM product_categories pc WHERE pc.parent_category_id IS NOT NULL AND id = :subCategoryId")
//    public Optional<ProductCategory> findSubById(@Param("subCategoryId") Long subCategoryId);
