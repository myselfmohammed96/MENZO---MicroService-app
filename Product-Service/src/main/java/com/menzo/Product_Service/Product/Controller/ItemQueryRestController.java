package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.AdminProductDetailsDto;
import com.menzo.Product_Service.Product.Dto.ItemDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-item")
public class ItemQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemQueryRestController.class);


    /*
     *
     *   Get all product-items
     *   Product-items identified by product ID
     *
     */
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(@RequestParam("id") Long productId) {
        AdminProductDetailsDto productDetails = productsRetrievalService.getProductDetailsWithAllItems(productId);
        return ResponseEntity.ok(productDetails);
    }


    /*
     *
     *   Get product-item
     *   Product-item identified by super SKU
     *
     */
    @GetMapping("/item")
    public ResponseEntity<?> getItem(@RequestParam("ssku") String superSku) {
        ItemDetailsDto itemDetails = productsRetrievalService.getItemDetails(superSku);
        return ResponseEntity.ok(itemDetails);
    }

}
