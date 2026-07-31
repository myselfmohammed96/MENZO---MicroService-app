package com.menzo.Product_Service.Product.Repo;

import com.menzo.Product_Service.Product.Entity.ProductConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductConfigurationRepo extends JpaRepository<ProductConfiguration, Long> {

    public List<ProductConfiguration> findAllByProductItemId(Long id);
}
