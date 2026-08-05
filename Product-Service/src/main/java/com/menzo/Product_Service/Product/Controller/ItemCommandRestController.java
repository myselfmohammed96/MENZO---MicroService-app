package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Dto.CreateProductItemDto;
import com.menzo.Product_Service.Product.Dto.ItemDto.PriceDto;
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
import org.springframework.web.server.ResponseStatusException;

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
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> addProductItem(@RequestHeader("roles") String roles,
                                                              @Valid @RequestPart("newItem") CreateProductItemDto itemDetails,
                                                              @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                                              @RequestPart("images") Map<String, MultipartFile> images) throws IOException {
        if (roles.equals("ADMIN")) {
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
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update product-item color
     *   Product-item identified by item ID
     *
     */
    @PutMapping("/update-color")
    public ResponseEntity<?> updateProductItemColor(@RequestHeader("roles") String roles,
                                                    @RequestParam("id") Long itemId,
                                                    @RequestParam("colorId") Long colorId) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }
            if (colorId == null || colorId <= 0) {
                logger.warn("Invalid color ID: {}", colorId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid color ID"));
            }

            //  update color
            boolean colorUpdated = itemCommandService.updateItemColor(itemId, colorId);

            //  response
            if (colorUpdated) {
                logger.info("Color updated successfully for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item color updated successfully."));
            } else {
                logger.error("Color update failed for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item color update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }


    /*
     *
     *   Update product-item size
     *   Product-item identified by item ID
     *
     */
    @PutMapping("/update-size")
    public ResponseEntity<?> updateProductItemSize(@RequestHeader("roles") String roles,
                                                   @RequestParam("id") Long itemId,
                                                   @RequestParam("sizeId") Long sizeId) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }
            if (sizeId == null || sizeId <= 0) {
                logger.warn("Invalid size ID: {}", sizeId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid size ID"));
            }

            //  update color
            boolean sizeUpdated = itemCommandService.updateItemSize(itemId, sizeId);

            //  response
            if (sizeUpdated) {
                logger.info("Size updated successfully for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item size updated successfully."));
            } else {
                logger.error("Size update failed for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item size update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }


    /*
     *
     *   Update product-item stock quantity
     *   Product-item identified by item ID
     *
     */
    @PutMapping("/update-stock-qty")
    public ResponseEntity<?> updateProductItemStockQuantity(@RequestHeader("roles") String roles,
                                                            @RequestParam("id") Long itemId,
                                                            @RequestParam("stockQty") Integer latestStockQty) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }
            if (latestStockQty == null || latestStockQty <= 0) {
                logger.warn("Invalid stock quantity: {}", latestStockQty);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid stock quantity"));
            }

            //  update stock quantity
            boolean stockQtyUpdated = itemCommandService.updateItemStockQuantity(itemId, latestStockQty);

            //  response
            if (stockQtyUpdated) {
                logger.info("Stock quantity updated successfully for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item stock quantity updated successfully."));
            } else {
                logger.error("Stock quantity update failed for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item stock quantity update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }


    /*
     *
     *   Update product-item prices
     *   Both selling price & MRP
     *   Product-item identified by item ID
     *
     */
    @PutMapping("/update-price")
    public ResponseEntity<?> updateProductItemPrice(@RequestHeader("roles") String roles,
                                                    @RequestParam("id") Long itemId,
                                                    @RequestParam("price") PriceDto latestPrice) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }

            //  update prices
            boolean pricesUpdated = itemCommandService.updateItemPrices(itemId, latestPrice);

            //  response
            if (pricesUpdated) {
                logger.info("Prices updated successfully for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item prices updated successfully."));
            } else {
                logger.error("Prices update failed for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item prices update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        }
    }


    /*
     *
     *   Update product-item active status
     *   Product-item identified by item ID
     *
     */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateProductItemActiveStatus(@RequestHeader("roles") String roles,
                                                           @RequestParam("id") Long itemId,
                                                           @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }

            //  update active status
            boolean updatedActive = itemCommandService.updateItemActiveStatus(itemId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for product-item ID: {}, updated successfully", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item active status updated successfully."));
            } else {
                logger.error("Active status update failed for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update product-item images
     *   Product-item identified by item ID
     *
     */
    @PutMapping(
            value = "/update-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateProductItemImages(@RequestHeader("roles") String roles,
                                                     @RequestParam("id") Long itemId,
                                                     @RequestPart("images") Map<String, MultipartFile> latestImages,
                                                     @RequestPart("imageIds") Map<String, Integer> imageIds) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }
            //  images validation
            if (latestImages == null || latestImages.isEmpty()) {
                throw new IllegalArgumentException("Product images required.");
            }
            if (latestImages.values().stream().anyMatch(file -> file == null || file.isEmpty()) || latestImages.keySet().stream().anyMatch(k -> k == null || k.trim().isEmpty())) {
                throw new IllegalArgumentException("Invalid product images");
            }
            if (latestImages.size() < 3) {
                throw new IllegalArgumentException("Minimum 3 images required.");
            }
            if (latestImages.size() > 9) {
                throw new IllegalArgumentException("You can upload a maximum of 9 images.");
            }

            //  update item images
            boolean imageUpdated = itemCommandService.updateItemImages(
                    itemId,
                    latestImages,
                    imageIds
            );

            //  response
            if (imageUpdated) {
                logger.info("Images updated for product-item ID: {}", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item images updated successfully."));
            } else {
                logger.error("Images update failed for product-item ID: {}", itemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Product-item images update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete product-item (soft delete)
     *   Product-item identified by item ID
     *
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProductItem(@RequestHeader("roles") String roles,
                                               @RequestParam("id") Long itemId) {
        if (roles.equals("ADMIN")) {
            //  input validation
            if (itemId == null || itemId <= 0) {
                logger.warn("Invalid product-item ID: {}", itemId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product-item ID"));
            }

            //  deleting product item
            boolean deleted = itemCommandService.deleteItem(itemId);

            //  response
            if (deleted) {
                logger.info("Product-item with ID {} deleted successfully", itemId);
                return ResponseEntity.ok(Map.of("message", "Product-item deleted successfully"));
            } else {
                logger.error("Product-item deletion failed for ID: {}", itemId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Product-item deletion failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
