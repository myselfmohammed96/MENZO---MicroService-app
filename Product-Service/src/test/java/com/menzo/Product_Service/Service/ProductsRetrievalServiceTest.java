package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.FilterDtos.RequestDto;
import com.menzo.Product_Service.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemSizeDto;
import com.menzo.Product_Service.Dto.ProductDto.UserProductDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class ProductsRetrievalServiceTest {

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @Test
    public void testGetAdminAllProductListing() {
        Page<AdminProductListingDto> adminAllProductListing = productsRetrievalService.getAdminProductListing(0,
                10,
                null,
                new RequestDto());

       System.out.println(adminAllProductListing);
    }

    @Test
    public void testGetItemDetails() {
        ItemDetailsDto itemDetails = productsRetrievalService.getItemDetails("FS-82-LBG");
        System.out.println(itemDetails.getStartingPrice());
        for(String url : itemDetails.getImageUrls()) {
            System.out.println(url);
        }
        for(ItemSizeDto dto : itemDetails.getSizeDetails()) {
            System.out.println(dto);
        }
    }

    @Test
    public void testGetUserProductDetails() {
        UserProductDetailsDto userProductDetails = productsRetrievalService.getUserProductDetails("C-90-BK");
        System.out.println(userProductDetails);
    }

//    @Test
//    public void testGetAllItems() {
//        List<AdminItemListingDto> allItems = productsRetrievalService.getAllItems(82L);
//        for (AdminItemListingDto item : allItems) {
//            System.out.println(item);
//        }
//    }

}