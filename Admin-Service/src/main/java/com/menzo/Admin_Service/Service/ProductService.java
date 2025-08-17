package com.menzo.Admin_Service.Service;

import com.menzo.Admin_Service.Dto.ProductListingDto;
import com.menzo.Admin_Service.Feign.ProductServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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
