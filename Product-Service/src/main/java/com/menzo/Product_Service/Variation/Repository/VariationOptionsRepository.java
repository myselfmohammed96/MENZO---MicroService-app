package com.menzo.Product_Service.Variation.Repository;

import com.menzo.Product_Service.Variation.Entity.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VariationOptionsRepository extends JpaRepository<VariationOption, UUID> {

    boolean existsByOptionValueAndVariationId(String optionValue, UUID variationId);     // TESTED

    List<VariationOption> findByOptionIdIn(List<UUID> idList);     // TESTED

    List<VariationOption> findByVariationId(UUID variationId);   // TESTED

    List<VariationOption> findByOptionValueContainingIgnoreCase(String keyword);

}

















//    public List<VariationOption> findAllById(List<Long> idList);