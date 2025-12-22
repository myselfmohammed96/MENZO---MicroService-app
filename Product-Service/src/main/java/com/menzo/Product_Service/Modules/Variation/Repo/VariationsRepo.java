package com.menzo.Product_Service.Modules.Variation.Repo;

import com.menzo.Product_Service.Modules.Variation.Entity.Variation;
import com.menzo.Product_Service.Modules.Variation.Dto.OptionMinimalDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariationsRepo extends JpaRepository<Variation, Long> {

    /*
     * ********* existence check *********
     */

    public boolean existsByVariationName(String variationName);     // TESTED



    /*
     *  ********* find methods *********
     */

    //  find variation by given variation name
    public Optional<Variation> findByVariationName(String variationName);   // TESTED

    //  find List of variations with variation-options - associated with given subCategoryId
//    @Query(
//            value =
//                    """
//                                SELECT
//                                        v.id AS variation_id,
//                                        v.variation_name,
//                                        o.id AS option_id,
//                                        o.option_value
//                                    FROM product_categories p
//                                    JOIN category_variation_configuration c
//                                        ON p.id = c.category_id
//                                    JOIN variations v
//                                        ON v.id = c.variation_id
//                                    JOIN variation_options o
//                                        ON v.id = o.variation_id
//                                    WHERE p.id = :subCategoryId
//                            """,
//            nativeQuery = true
//    )
//    public List<Object[]> findAllBySubCategoryId(@Param("subCategoryId") Long subCategoryId);          // TESTED

    @Query(
            value = """
                    SELECT\s
                    		v.id AS variation_id,\s
                    		v.variation_name,\s
                    		o.id AS option_id,\s
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
                    	WHERE (:isParent = TRUE AND p.id = :categoryId 
                    	    OR :isParent = FALSE AND s.id = :categoryId)
                    	GROUP BY option_value 
                    """,
            nativeQuery = true
    )
    public List<Object[]> findAllByCategoryId(@Param("categoryId") Long categoryId,
                                              @Param("isParent") boolean isParent);

    //  find variation-options by given variation name
    @Query("""
                    SELECT new com.menzo.Product_Service.Modules.Variation.Dto.OptionMinimalDto(o.id, o.optionValue)
                    FROM Variation v
                    JOIN v.options o
                    WHERE v.variationName = :variationName
            """)
    public List<OptionMinimalDto> findOptionsByVariationName(@Param("variationName") String variationName);      // TESTED
}
