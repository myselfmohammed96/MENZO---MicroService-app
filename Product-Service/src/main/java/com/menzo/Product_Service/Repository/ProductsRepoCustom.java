package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.FilterDtos.QueryDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingView;

import java.util.List;

public interface ProductsRepoCustom {

    List<ProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails);

}
