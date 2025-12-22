package com.menzo.Product_Service.Modules.Discount.Repo;

import com.menzo.Product_Service.Modules.Discount.Entity.Discount;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, UUID> {

    boolean existsByDiscountCode(@NotBlank String discountCode);

}
