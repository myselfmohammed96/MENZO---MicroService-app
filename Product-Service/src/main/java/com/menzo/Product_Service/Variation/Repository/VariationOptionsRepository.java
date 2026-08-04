package com.menzo.Product_Service.Variation.Repository;

import com.menzo.Product_Service.Variation.Entity.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationOptionsRepository extends JpaRepository<VariationOption, Long> {

    boolean existsByOptionValueAndVariationId(String optionValue, Long variationId);     // TESTED

    List<VariationOption> findByOptionIdIn(List<Long> idList);     // TESTED

    List<VariationOption> findByVariationId(Long variationId);   // TESTED

    List<VariationOption> findByOptionValueContainingIgnoreCase(String keyword);

}

















//    public List<VariationOption> findAllById(List<Long> idList);