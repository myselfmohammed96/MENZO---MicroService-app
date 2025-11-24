package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.FilterDtos.RequestDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemListingDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemSizeDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

@SpringBootTest
class ProductsRetrievalServiceTest {

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @Test
    public void testGetAdminAllProductListing() {
        Page<ProductListingDto> adminAllProductListing = productsRetrievalService.getAdminAllProductListing(0,
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

//    @Test
//    public void testGetAllItems() {
//        List<ItemListingDto> allItems = productsRetrievalService.getAllItems(82L);
//        for (ItemListingDto item : allItems) {
//            System.out.println(item);
//        }
//    }

}