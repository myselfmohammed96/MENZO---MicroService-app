package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.ColorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorCodeRepo extends JpaRepository<ColorCode, Long> {

    public boolean existsByColorAbbreviation(String abb);
}
