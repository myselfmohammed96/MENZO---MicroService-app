package com.menzo.Product_Service.Product.Dto.ProductDto;

import java.util.Date;
import java.util.UUID;

public interface ProductListingView {

    UUID getProductId();

    String getProductName();

    String getSubCategoryName();

    String getCategoryName();

    Float getMinPrice();        //  must be BigDecimal probably

    Float getMaxPrice();

    Integer getMinStockQty();

    Integer getMaxStockQty();

    Date getLatestCreatedAt();

    Date getOldestCreatedAt();

    String getActiveStatus();

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
//
//    String getIconImage();

}
