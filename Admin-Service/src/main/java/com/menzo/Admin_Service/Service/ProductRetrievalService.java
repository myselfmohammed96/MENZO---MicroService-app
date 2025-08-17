package com.menzo.Admin_Service.Service;

import com.menzo.Admin_Service.Dto.ProductDetailsDto;
import com.menzo.Admin_Service.Dto.ProductMinimalDto;
import com.menzo.Admin_Service.Feign.ProductServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductRetrievalService {

    @Autowired
    private ProductServiceFeign productFeign;

    public ProductMinimalDto getProductById(Long productId) {
        return productFeign.getProductById(productId);
    }

    public ProductDetailsDto getProductDetailsById(Long productId) {
        return productFeign.getProductDetailsById(productId);
    }
}
