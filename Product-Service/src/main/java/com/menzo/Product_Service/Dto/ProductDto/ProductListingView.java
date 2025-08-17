package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;

public interface ProductListingView {

    Long getId();
    String getProductName();
    String getSubCategoryName();
    Float getStartingPrice();
    Integer getTotalQty();
    ProductActiveStatus getActiveStatus();
    String getIconImage();
}
