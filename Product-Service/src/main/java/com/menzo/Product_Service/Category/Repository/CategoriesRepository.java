package com.menzo.Product_Service.Category.Repository;

import com.menzo.Product_Service.Category.Dto.ParentCategoryView;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<ProductCategory, Long> {


    //  ********* existence check *********


    /*
     *
     *   Common existence check method
     *   Checked with category name
     *
     */
    boolean existsByCategoryName(String categoryName);       //  TESTED


    /*
     *
     *   Existence check for sub-category
     *   Checked with sub-category abbreviation
     *
     */
    boolean existsByAbbreviation(String abbreviation);       //  TESTED


    /*
     *
     *   Existence check for sub-category
     *   Checked with category name & parent category ID
     *
     */
    boolean existsByCategoryNameAndParentCategory_CategoryId(String categoryName, Long parentCategoryId);     //  TESTED


    List<ProductCategory> findByCategoryNameContainingIgnoreCase(String keyword);


    /*
     *
     *   Find all parent categories
     *
     */
    List<ProductCategory> findByParentCategory_CategoryIdIsNull();        //  TESTED


    /*
     *
     *   Find parent category
     *   Parent category identified by category ID
     *
     */
    Optional<ProductCategory> findByIdAndParentCategory_CategoryIdIsNull(Long id);


    /*
     *
     *   Find all parent categories
     *   With their sub-categories
     *   Filtering soft deleted data (params must be false, false)
     *
     */
    @Query(
            nativeQuery = true,
            value =
                    """
                            SELECT\s
                                    p.category_id AS parentId,\s
                                    p.category_name AS parentName,\s
                                    s.category_id AS subId,\s
                                    s.category_name AS subName\s
                                FROM product_categories p\s
                                LEFT JOIN product_categories s\s
                                    ON s.parent_category_id = p.category_id\s
                                WHERE p.parent_category_id IS NULL\s
                                    AND p.is_deleted = :isParentDeleted\s
                                    AND s.is_deleted = :isSubDeleted\s
                                ORDER BY p.category_id, s.category_id
                            """)
    List<Object[]> findAllParentWithSub(@Param("isParentDeleted") boolean isParentDeleted,
                                        @Param("isSubDeleted") boolean isSubDeleted);                                   // TESTED


    /*
     *
     *   Find parent category with sub-categories
     *   Parent category identified by parent category ID
     *
     */
    @Query(
            nativeQuery = true,
            value =
                    """
                            SELECT\s
                                    p.category_id AS parentId,\s
                                    p.category_name AS parentName,\s
                                    s.category_id AS subId,\s
                                    s.category_name AS subName\s
                                FROM product_categories p\s
                                LEFT JOIN product_categories s\s
                                    ON s.parent_category_id = p.category_id\s
                                WHERE p.parent_category_id IS NULL\s
                                    AND p.category_id = :parentCategoryId\s
                                    AND p.is_deleted = :isParentDeleted\s
                                    AND s.is_deleted = :isSubDeleted\s
                                ORDER BY p.category_id, s.category_id
                            """)
    List<Object[]> findParentByIdWithSub(@Param("parentCategoryId") Long parentCategoryId,
                                         @Param("isParentDeleted") boolean isParentDeleted,
                                         @Param("isSubDeleted") boolean isSubDeleted);  // TESTED


    /*
     *
     *   Find parent category
     *   Parent category identified by sub-category ID
     *
     */
    @Query(
            nativeQuery = true,
            value =
                    """
                            SELECT 
                                    p.category_id AS categoryId,\s
                                    p.category_name AS categoryName\s
                                FROM product_categories p\s
                                WHERE p.is_active = :isParentActive\s
                                    AND p.is_deleted = :isParentDeleted\s
                                    AND p.category_id = (\s
                                        SELECT\s
                                                s.parent_category_id\s
                                            FROM product_categories s\s
                                            WHERE s.is_active = :isSubActive\s
                                                AND s.is_deleted = :isSubDeleted\s
                                                AND s.parent_category_id IS NOT NULL\s
                                                AND s.category_id = :subCategoryId\s
                                    )
                            """)
    ParentCategoryView findParentCategoryBySubId(@Param("isParentActive") int isParentActive,
                                                 @Param("isParentDeleted") int isParentDeleted,
                                                 @Param("isSubActive") int isSubActive,
                                                 @Param("isSubDeleted") int isSubDeleted,
                                                 @Param("subCategoryId") Long subCategoryId);        //  TESTED


    /*
     *
     *   Find parent category
     *   Parent category identified by product ID
     *
     */
    @Query(nativeQuery = true,
            value =
                    """
                                SELECT 
                                        p.category_id AS categoryId,\s
                                        p.category_name as categoryName\s
                                    FROM product_categories p\s
                                    JOIN product_categories s\s
                                        ON c.category_id = sc.parent_category_id\s
                                    JOIN products pr\s
                                        ON s.category_id = pr.category_id\s
                                    WHERE p.is_active = :isParentActive\s
                                        AND p.is_deleted = :isParentDeleted\s
                                        AND s.is_active = :isSubActive\s
                                        AND s.is_deleted = :isSubDeleted\s
                                        AND pr.is_active = :isProductActive\s
                                        AND pr.is_deleted = :isProductDeleted\s
                                        AND pr.product_id = :productId
                            """)
    ParentCategoryView findParentByProductId(@Param("isParentActive") int isParentActive,
                                             @Param("isParentDeleted") int isParentDeleted,
                                             @Param("isSubActive") int isSubActive,
                                             @Param("isSubDeleted") int isSubDeleted,
                                             @Param("isProductActive") int isProductActive,
                                             @Param("isProductDeleted") int isProductDeleted,
                                             @Param("productId") Long productId);


//    @Query(
//            value = """
//                    SELECT * FROM product_categories c
//                    WHERE
//                        (:checkNull = TRUE AND c.parent_category_id IS NULL
//                        OR :checkNull = FALSE AND c.parent_category_id IS NOT NULL)
//                    """,
//            nativeQuery = true
//    )
//    List<ProductCategory> findIt(@Param("checkNull") boolean checkNull);


    /*
     *   ********* Sub-categories *********
     */


    /*
     *
     *   Find sub-category
     *   Sub-category identified by category ID
     *
     */
    Optional<ProductCategory> findByIdAndParentCategory_CategoryIdIsNotNull(Long id);         // TESTED


    /*
     *
     *   Find all sub-categories
     *   Sub-categories identified by parent category ID
     *
     */
    List<ProductCategory> findAllByParentCategory_CategoryId(Long parentCategoryId);          // TESTED


//    //  returns the count of entities of the ids in the list
//    public long countByIdIn(List<Long> selectionList);

    //  returns list of available entities with provided idList
    List<ProductCategory> findByIdIn(List<Long> selectionList);



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
//    SubCategoryDto findSubByIdWithoutVariation(@Param("subCategoryId") Long subCategoryId);

}


//    @Query("SELECT new com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto(pc.id, pc.parentCategoryId, pc.categoryName, " +
//            "pc.isActive, pc.createdAt) FROM ProductCategory pc WHERE pc.parentCategoryId = :parentId")
//    List<SubCategoryDto> findAllSubByParentId(@Param("parentId") Long parentId);


/// *
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
//    void deleteParentById(@Param("parentCategoryId") Long parentCategoryId);
//
//    @Query(
//            nativeQuery = true,
//            value = "DELETE FROM product_categories " +
//                    "WHERE parent_category_id IS NOT NULL " +
//                    "AND id = :subCategoryId"
//    )
//    void deleteSubById(@Param("subCategoryId") Long subCategoryId);


//    @Query(nativeQuery = true, value = "SELECT pc.id, pc.parent_category_id, pc.category_name, pc.is_active, pc.created_at " +
//            "FROM product_categories pc WHERE pc.parent_category_id IS NOT NULL AND id = :subCategoryId")
//    Optional<ProductCategory> findSubById(@Param("subCategoryId") Long subCategoryId);
