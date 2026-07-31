package com.menzo.Product_Service.Variation.Repo;

import com.menzo.Product_Service.Variation.Entity.ColorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorCodeRepository extends JpaRepository<ColorCode, Long> {

    boolean existsByColorAbbreviation(String abb);       // TESTED
}
