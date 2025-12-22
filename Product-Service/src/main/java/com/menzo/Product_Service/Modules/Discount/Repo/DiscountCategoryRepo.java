package com.menzo.Product_Service.Modules.Discount.Repo;

import com.menzo.Product_Service.Modules.Discount.Entity.DiscountCategory;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountCategoryRepo extends JpaRepository<DiscountCategory, UUID> {
}
