package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationsOptionsRepo extends JpaRepository<VariationOption, Long> {

    public boolean existsByOptionValueAndVariationId(String optionValue, Long variationId);     // TESTED

    public List<VariationOption> findByIdIn(List<Long> idList);     // TESTED

    public List<VariationOption> findByVariationId(Long variationId);   // TESTED
}

















//    public List<VariationOption> findAllById(List<Long> idList);