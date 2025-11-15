package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.FilterDtos.QueryDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductsRepoCustom {

    Page<ProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails);

}
