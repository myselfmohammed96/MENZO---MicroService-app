package com.menzo.User_Service.Feign;

import com.menzo.User_Service.Wishlist.Dto.WishlistDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("PRODUCT-SERVICE")
public interface ProductFeign {

    /*
    *
    *   Get product-item sku
    *   Product-item identified by item ID
    *
     */
    @GetMapping("/product-item/get-sku")
    public ResponseEntity<String> getSkuByItemId(@RequestParam("id") Long itemId);


    /*
    *
    *   Get wishlist-item data (product-item details)
    *
     */
    @GetMapping("/product-item/get-wishlist-data")
    public ResponseEntity<List<WishlistDto>> getWishlistItemsData(@RequestParam("item-ids") List<Long> wishlistItemIds);
}
