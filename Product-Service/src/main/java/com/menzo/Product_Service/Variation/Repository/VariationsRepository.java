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
    Optional<Variation> findByVariationName(String variationName);   // TESTED


    /*
     *
     *   Find list of variations with variation options
     *   Associated with category or sub-category
     *   Identified using category or sub-category ID
     *   Also used to find soft deleted and not soft deleted variations
     *
     */
    @Query(
            value = """
                    SELECT\s
                    		v.variation_id AS variation_id,\s
                    		v.variation_name,\s
                    		o.option_id AS option_id,\s
                    		o.option_value 
                    	FROM product_categories p 
                    	JOIN product_categories s 
                    		ON p.id = s.parent_category_id 
                    	JOIN category_variation_configuration c 
                    		ON s.id = c.category_id 
                    	JOIN variations v\s
                    		ON v.id = c.variation_id 
                    	JOIN variation_options o\s
                    		ON v.id = o.variation_id 
                    	WHERE (
                            (:isParent = TRUE AND p.id = :categoryId AND p.isDeleted = :isDeleted) 
                    	    OR 
                            (:isParent = FALSE AND s.id = :categoryId AND s.isDeleted = :isDeleted)
                        )
                    	GROUP BY option_value 
                    """,
            nativeQuery = true
    )
    List<Object[]> findAllByCategoryId(@Param("categoryId") Long categoryId,
                                       @Param("isParent") boolean isParent,
                                       @Param("isDeleted") boolean isDeleted);


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
