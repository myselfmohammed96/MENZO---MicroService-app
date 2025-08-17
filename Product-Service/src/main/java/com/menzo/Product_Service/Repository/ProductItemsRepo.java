package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductItemsRepo extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {

    public List<ProductItem> findAllByProductId(Long productId);

    public Page<ProductItem> findAllByProductId(Long productId, Pageable pageable);

}
