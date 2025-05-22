package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.Variations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariationsRepo extends JpaRepository<Variations, Long> {
}
