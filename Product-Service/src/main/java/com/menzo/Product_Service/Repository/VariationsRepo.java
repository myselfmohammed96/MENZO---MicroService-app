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

    public Optional<Variation> findVariationById(Long variationId);

    @Query(value = "SELECT new com.menzo.Product_Service.Dto.VariationsDto.VariationDto(v.id, v.variationName) FROM Variation v")
    public List<VariationDto> findAllVariationIdAndNames();

    public void deleteVariationById(Long variationId);

    public boolean existsById(Long variationId);

    public boolean existsByVariationName(String variationName);

    @Query(value = "SELECT v.id AS variation_id, v.variation_name, " +
            "o.id AS option_id, o.option_value " +
            "FROM product_categories p " +
            "JOIN category_variation_configuration c ON p.id = c.category_id " +
            "JOIN variations v ON v.id = c.variation_id " +
            "JOIN variation_options o ON v.id = o.variation_id " +
            "WHERE p.id = :subCategoryId", nativeQuery = true)
    public List<Object[]> findAllByCategoryId(@Param("subCategoryId") Long subCategoryId);

    @Query("""
            SELECT new com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto(o.id, o.optionValue)
            FROM Variation v
            JOIN v.options o
            WHERE v.variationName = :variationName
    """)
    public List<OptionWithIdDto> findOptionsByVariationName(@Param("variationName") String variationName);
}
