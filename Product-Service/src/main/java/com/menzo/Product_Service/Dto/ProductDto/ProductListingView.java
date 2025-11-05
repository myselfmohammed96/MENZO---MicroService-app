package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;

import java.util.Date;

public interface ProductListingView {

//    Long getId();
//    String getProductName();
//    String getSubCategoryName();
//    Float getStartingPrice();
//    Integer getTotalQty();
//    ProductActiveStatus getActiveStatus();
//    String getIconImage();

    Long getProductId();

    String getProductName();

    String getSubCategoryName();

    String getCategoryName();

    Float getMinPrice();

    Float getMaxPrice();

    Integer getMinStockQty();

    Integer getMaxStockQty();

    Date getLatestCreatedAt();

    Date getOldestCreatedAt();

    String getActiveStatus();


//
//    Boolean getPodAvailable();
//
//    Date getProductCreateAt();
//
//    Long getItemId();
//
//    String getItemSuperSku();
//
//    Float getItemPrice();
//
//    Integer getItemStock();
//
//    String getItemSku();
//
//    Boolean getItemActive();
}
