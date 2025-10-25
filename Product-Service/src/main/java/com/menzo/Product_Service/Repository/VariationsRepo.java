package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
import com.menzo.Product_Service.Entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    //  ## rename to findAllBySubCategoryId
    @Query(
            value =
                    "SELECT v.id AS variation_id, v.variation_name, " +
                            "o.id AS option_id, o.option_value " +
                            "FROM product_categories p " +
                            "JOIN category_variation_configuration c " +
                            "ON p.id = c.category_id " +
                            "JOIN variations v " +
                            "ON v.id = c.variation_id " +
                            "JOIN variation_options o " +
                            "ON v.id = o.variation_id " +
                            "WHERE p.id = :subCategoryId",
            nativeQuery = true
    )
    public List<Object[]> findAllByCategoryId(@Param("subCategoryId") Long subCategoryId);          // TESTED

    //  find variation-options by given variation name
    @Query("""
            SELECT new com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto(o.id, o.optionValue)
            FROM Variation v
            JOIN v.options o
            WHERE v.variationName = :variationName
    """)
    public List<OptionWithIdDto> findOptionsByVariationName(@Param("variationName") String variationName);      // TESTED
}
