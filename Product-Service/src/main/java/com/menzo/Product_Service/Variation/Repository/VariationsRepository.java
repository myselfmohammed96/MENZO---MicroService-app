package com.menzo.Product_Service.Variation.Repository;

import com.menzo.Product_Service.Variation.Entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariationsRepository extends JpaRepository<Variation, Long> {


    /*
     *
     *  Variations existence check
     *  By variation name
     *
     */
    boolean existsByVariationName(String variationName);     // TESTED


    /*
     *
     *  Find variations
     *  Identified using variation name
     *
     */
    Optional<Variation> findByVariationNameIgnoreCase(String variationName);   // TESTED


    /*
     *
     *   Find list of variations with variation options
     *   Associated with category or sub-category
     *   Identified using category or sub-category ID
     *   Also used to find soft deleted and not soft deleted variations
     *
     */
    @Query(
            nativeQuery = true,
            value = """
                    SELECT\s
                    		v.variation_id AS variation_id,\s
                    		v.variation_name,\s
                    		o.option_id AS option_id,\s
                    		o.option_value\s
                    	FROM product_categories p\s
                    	JOIN product_categories s\s
                    		ON p.category_id = s.parent_category_id\s
                    	JOIN category_variation_configuration cv\s
                    		ON s.category_id = cv.category_id\s
                    	JOIN variations v\s
                    		ON v.variation_id = cv.variation_id\s
                    	JOIN variation_options o\s
                    		ON v.variation_id = o.variation_id\s
                    	WHERE v.is_active = :isVariationActive\s
                            AND v.is_deleted = :isVariationDeleted\s
                            AND o.is_active = :isOptionActive\s
                            AND o.is_deleted = :isOptionDeleted\s
                            AND (\s
                                    (\s
                                        :isParent = TRUE\s
                                            AND p.category_id = :categoryId\s
                                            AND p.is_active = :isParentActive\s
                                            AND p.is_deleted = :isParentDeleted\s
                                    )\s
                    	            OR\s
                                    (\s
                                        :isParent = FALSE\s
                                            AND s.category_id = :categoryId\s
                                            AND s.is_active = :isSubActive\s
                                            AND s.is_deleted = :isSubDeleted\s
                                    )\s
                            )\s
                    	GROUP BY option_value
                    """)
    List<Object[]> findAllByCategoryId(@Param("isParentActive") int isParentActive,
                                       @Param("isParentDeleted") int isParentDeleted,
                                       @Param("isSubActive") int isSubActive,
                                       @Param("isSubDeleted") int isSubDeleted,
                                       @Param("isVariationActive") int isVariationActive,
                                       @Param("isVariationDeleted") int isVariationDeleted,
                                       @Param("isOptionActive") int isOptionActive,
                                       @Param("isOptionDeleted") int isOptionDeleted,
                                       @Param("isParent") boolean isParent,
                                       @Param("categoryId") Long categoryId);

























    /*
     *
     *   Find variation options
     *   Identified by variation name
     *   ## must be moved to options repo
     *
     */
//    @Query("""
//                    SELECT new com.menzo.Product_Service.Modules.Variation.Dto.OptionMinimalDto(o.id, o.optionValue)
//                    FROM Variation v
//                    JOIN v.options o
//                    WHERE v.variationName = :variationName
//            """)
//    List<OptionMinimalDto> findOptionsByVariationName(@Param("variationName") String variationName);      // TESTED

}
