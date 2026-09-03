package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.Product.Entity.ProductConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration, UUID> {

    public List<ProductConfiguration> findAllByProductItem_ItemId(UUID id);
}
