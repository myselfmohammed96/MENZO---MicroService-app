package com.menzo.Product_Service.Discount.Repository;

import com.menzo.Product_Service.Discount.Entity.DiscountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountCategoryRepository extends JpaRepository<DiscountCategory, UUID> {
}
