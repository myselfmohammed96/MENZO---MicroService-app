package com.menzo.Admin_Service.Modules.Product.Service;

import com.menzo.Admin_Service.Modules.Product.Dto.ProductListingDto;
import com.menzo.Admin_Service.Modules.Product.Feign.ProductServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductServiceFeign productServiceFeign;

//    public Page<ProductListingDto> getAllProductsListingWithPagination(Integer page, Integer size) {
//        Page<ProductListingDto> productslisting = productServiceFeign.getAllProductsListingWithPagination(page, size);
//        return productslisting;
//    }

    public Page<ProductListingDto> getAllProductsListing(Integer page, Integer size) {
        return productServiceFeign.getAllProductListingWithPagination(page, size);
    }
}
