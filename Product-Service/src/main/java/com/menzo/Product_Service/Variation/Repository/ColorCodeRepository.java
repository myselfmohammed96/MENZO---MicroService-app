package com.menzo.Product_Service.Variation.Repository;

import com.menzo.Product_Service.Variation.Entity.ColorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ColorCodeRepository extends JpaRepository<ColorCode, UUID> {

    boolean existsByColorAbbreviation(String abb);       // TESTED

}
