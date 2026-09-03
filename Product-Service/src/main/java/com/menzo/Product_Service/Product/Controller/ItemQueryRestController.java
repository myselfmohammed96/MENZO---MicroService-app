package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.ProductDto.AdminProductDetailsDto;
import com.menzo.Product_Service.Product.Dto.ItemDto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Service.ItemQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/product-item")
public class ItemQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemQueryRestController.class);

    @Autowired
    private ItemQueryService itemQueryService;


    /*
     *
     *   Get all product-items
     *   Product-items identified by product ID
     *
     */
//    @GetMapping("/items")
//    public ResponseEntity<?> getAllItems(@RequestParam("id") Long productId) {
//        AdminProductDetailsDto productDetails = itemQueryService.getProductDetailsWithAllItems(productId);
//        return ResponseEntity.ok(productDetails);
//    }


    /*
     *
     *   Get product-item
     *   Product-item identified by super SKU
     *
     */
    @GetMapping("/item")
    public ResponseEntity<?> getItem(@RequestParam("ssku") String superSku) {
        ItemDetailsDto itemDetails = itemQueryService.getItemDetails(superSku);
        return ResponseEntity.ok(itemDetails);
    }

    @GetMapping("/get-sku")
    public ResponseEntity<String> getSkuByItemId(@RequestParam("id") UUID itemId) {
        String sku = itemQueryService.getSkuByItemId(itemId);
        return ResponseEntity.ok(sku);
    }

}
