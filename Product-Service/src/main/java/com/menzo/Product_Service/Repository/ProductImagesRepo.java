package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImagesRepo extends JpaRepository<ProductImage, Long> {

    public List<ProductImage> findByProductId(Long productId);

    public List<ProductImage> findByProductItemId(Long productItemId);
}
