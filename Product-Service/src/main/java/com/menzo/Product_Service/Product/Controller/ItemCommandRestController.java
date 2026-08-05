package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Dto.CreateProductItemDto;
import com.menzo.Product_Service.Product.Dto.SizeDetailsDto;
import com.menzo.Product_Service.Product.Service.ItemCommandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product-item")
public class ItemCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemCommandRestController.class);

    @Autowired
    private ItemCommandService itemCommandService;


    /*
     *
     *   Add new product item
     *   Product item belongs to a product
     *   Product identified by product ID
     *
     */
    @PostMapping(
            value = "/add-item",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> addProductItem(@Valid @RequestPart("newItem") CreateProductItemDto itemDetails,
                                                              @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                                              @RequestPart("images") Map<String, MultipartFile> images) throws IOException {
        //  product details validation
        if (itemDetails == null) {
            throw new IllegalArgumentException("Product item details not found.");
        }

        //  size details validation
        if (sizeDetails == null || sizeDetails.isEmpty()) {
            throw new IllegalArgumentException("Size details required.");
        }

        //  images validation
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Product images required.");
        }
        if (images.values().stream().anyMatch(file -> file == null || file.isEmpty()) || images.keySet().stream().anyMatch(k -> k == null || k.trim().isEmpty())) {
            throw new IllegalArgumentException("Invalid product images");
        }
        if (images.size() < 3) {
            throw new IllegalArgumentException("Minimum 3 images required.");
        }
        if (images.size() > 9) {
            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        }

        //  save new product item
        ItemDetailsDto savedItemDetails = itemCommandService.addNewProductItem(
                itemDetails,
                sizeDetails,
                images
        );

        //  response
        Map<String, Object> responseBody = new HashMap<>();
        if (itemDetails != null) {
            logger.info("Product item saved successfully with super SKU: {}", savedItemDetails.getSuperSku());
            responseBody.put("message", "Product item saved successfully");
            responseBody.put("itemDetails", savedItemDetails);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseBody);
        } else {
            logger.warn("Product item saving failed");
            responseBody.put("message", "Product item saving failed");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *
     *   Update product-item
     *   Product-item identified by item ID
     *
     */
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateProductItem() {}


    /*
     *
     *   Update product-item active status
     *   Product-item identified by item ID
     *
     */
    @PutMapping(value = "/update-status")
    public ResponseEntity<?> updateProductItemActiveStatus() {}


    /*
     *
     *   Delete product-item (soft delete)
     *   Product-item identified by item ID
     *
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProductItem() {}

}
