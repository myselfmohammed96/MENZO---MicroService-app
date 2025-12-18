package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.QueryDetailsDto;
import com.menzo.Product_Service.Modules.Product.Dto.AdminProductListingDto;
import com.menzo.Product_Service.Modules.Product.Dto.UserProductListingDto;
import org.springframework.data.domain.Page;

public interface ProductsRepoCustom {

    Page<AdminProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails);

    Page<UserProductListingDto> findUserProductListing(QueryDetailsDto queryDetails);

}
