package com.menzo.Admin_Service.Feign;

import com.menzo.Admin_Service.Dto.ProductDetailsDto;
import com.menzo.Admin_Service.Dto.ProductListingDto;
import com.menzo.Admin_Service.Dto.ProductMinimalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8986")
public interface ProductServiceFeign {

    @GetMapping("/products/all-products-listing")
    public Page<ProductListingDto> getAllProductListingWithPagination (@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size);

    @GetMapping("/products/get-by-id")
    public ProductMinimalDto getProductById(@RequestParam("id") Long productId);

    @GetMapping("/products/get-details-by-id")
    public ProductDetailsDto getProductDetailsById(@RequestParam("id") Long productId);

//    @GetMapping("/categories/")
//    public void getSubCategoryByProductId(@RequestParam("id") Long productId);

//    public List<ProductListingDto> getAllProductsListingWithPagination(@RequestParam(defaultValue = "0") Integer page,
//                                                                       @RequestParam(defaultValue = "10") Integer size);
}
