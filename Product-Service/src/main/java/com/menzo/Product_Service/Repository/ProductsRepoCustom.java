package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.FilterDtos.QueryDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.Dto.ProductDto.UserProductListingDto;
import org.springframework.data.domain.Page;

public interface ProductsRepoCustom {

    Page<AdminProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails);

    Page<UserProductListingDto> findUserProductListing(QueryDetailsDto queryDetails);

}
