package com.menzo.User_Service.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PRODUCT-SERVICE")
public interface ProductFeign {

    /*
    *
    *   Get product-item sku
    *   Product-item identified by item ID
    *
     */
    @GetMapping("/get-sku")
    public ResponseEntity<String> getSkuByItemId(@RequestParam("id") Long itemId);

}
