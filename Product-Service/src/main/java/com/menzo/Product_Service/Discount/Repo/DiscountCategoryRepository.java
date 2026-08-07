package com.menzo.Product_Service.Discount.Repo;

import com.menzo.Product_Service.Discount.Entity.DiscountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountCategoryRepo extends JpaRepository<DiscountCategory, UUID> {
}
