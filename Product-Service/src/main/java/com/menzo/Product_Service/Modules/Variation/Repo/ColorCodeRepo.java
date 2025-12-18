package com.menzo.Product_Service.Modules.Variation.Repo;

import com.menzo.Product_Service.Modules.Variation.Entity.ColorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorCodeRepo extends JpaRepository<ColorCode, Long> {

    public boolean existsByColorAbbreviation(String abb);       // TESTED
}
