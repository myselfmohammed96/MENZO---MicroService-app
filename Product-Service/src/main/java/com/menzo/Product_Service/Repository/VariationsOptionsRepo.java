package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.VariationOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariationsOptionsRepo extends JpaRepository<VariationOption, Long> {

    public boolean existsByOptionValueAndVariationId(String optionValue, Long variationId);
}
