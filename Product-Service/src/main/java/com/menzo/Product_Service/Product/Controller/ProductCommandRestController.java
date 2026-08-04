package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.*;
import com.menzo.Product_Service.Product.Dto.ProductDto.CreateProductDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.ProductDto;
import com.menzo.Product_Service.Product.Service.ProductCommandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProductCommandRestController.class);

    @Autowired
    private ProductCommandService productCommandService;


    /*
     *
     *   Add new product
     *
     */
    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> addNewProduct(@Valid @RequestPart("productDetails") CreateProductDto productDetails,
                                           @Valid @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                           BindingResult result,
                                           @RequestPart("variationDetails") Map<String, String> variationDetailsMap,
                                           @RequestPart("images") Map<String, MultipartFile> images) throws IOException {
        //  product details validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            logger.warn("Validation failed for new product: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        //  size details validation
        if (sizeDetails == null || sizeDetails.isEmpty()) {
            throw new IllegalArgumentException("Size details required.");
        }

        //  variation details map validation
        if (variationDetailsMap == null || variationDetailsMap.isEmpty()) {
            throw new IllegalArgumentException("Variation details required.");
        }
        if (variationDetailsMap.values().stream().anyMatch(v -> v == null || v.trim().isEmpty())) {
            throw new IllegalArgumentException("Invalid variation value");
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

        Long savedProductId = productCommandService.addNewProduct(
                productDetails,
                sizeDetails,
                variationDetailsMap,
                images
        );

        //  response
        Map<String, Object> responseBody = new HashMap<>();
        if (savedProductId != null && savedProductId > 0) {
            logger.info("Product saved successfully with ID: {}", savedProductId);
            responseBody.put("message", "Product saved successfully");
            responseBody.put("productId", savedProductId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseBody);
        } else {
            logger.warn("Product saving failed");
            responseBody.put("message", "Product saving failed");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *
     *   Update product
     *   Product identified by product ID
     *
     */
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateProductDetails(@RequestParam("id") Long productId,
                                                  @RequestBody ProductDto latestProduct) {
        //  input validation
        if (productId == null || productId <= 0) {
            logger.warn("Invalid product ID: {}", productId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Product ID"));
        }

        //  updating product
        boolean productUpdated = productCommandService.updateProductDetails(productId, latestProduct);

        //  response
        if (productUpdated) {
            logger.info("Product with ID {} updated successfully", productId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Product updated successfully"));
        } else {
            logger.error("Product update failed for ID: {}", productId);
            return ResponseEntity.ok(Map.of("message", "Failed to update product"));
        }
    }


    /*
     *
     *   Update product active status
     *   Product identified by product ID
     *
     */
    @PutMapping(value = "/update-status")
    public ResponseEntity<?> updateProductActiveStatus(@RequestHeader("roles") String roles,
                                                       @RequestParam("id") Long productId,
                                                       @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (productId == null || productId <= 0) {
                logger.warn("Invalid product ID: {}", productId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product ID"));
            }

            //  update active status
            boolean updatedActive = productCommandService.updateProductActiveStatus(productId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for product ID: {}, updated successfully", productId);
                return ResponseEntity.ok(Map.of("message", "Product active status updated successfully."));
            } else {
                logger.error("Active status update failed for product ID: {}", productId);
                return ResponseEntity.ok(Map.of("message", "Product active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update product approval status
     *   Product identified by product ID
     *
     */
    @PutMapping("/update-approval")
    public ResponseEntity<?> updateProductApprovalStatus(@RequestHeader("roles") String roles,
                                                         @RequestHeader("email") String staffEmail,
                                                         @RequestParam("id") Long productId,
                                                         @RequestParam("approved") boolean isApproved) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (productId == null || productId <= 0) {
                logger.warn("Invalid product ID: {}", productId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product ID"));
            }
            if (staffEmail == null || staffEmail.isEmpty()) {
                throw new IllegalArgumentException("Staff email address required.");
            }

            //  update active status
            boolean updatedApprovalStatus = productCommandService.updateProductApprovalStatus(
                    staffEmail,
                    productId,
                    isApproved
            );

            //  response
            if (isApproved == updatedApprovalStatus) {
                logger.info("Approval status for product ID: {}, updated successfully", productId);
                return ResponseEntity.ok(Map.of("message", "Product approved successfully."));
            } else {
                logger.error("Approval failed for product ID: {}", productId);
                return ResponseEntity.ok(Map.of("message", "Product approval failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update product POD status
     *   product identified by product ID
     *
     */
    @PutMapping("/update-pod")
    public ResponseEntity<?> updateProductPodStatus(@RequestHeader("roles") String roles,
                                                    @RequestParam("id") Long productId,
                                                    @RequestParam("podAvailable") boolean podAvailable) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (productId == null || productId <= 0) {
                logger.warn("Invalid product ID: {}", productId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid product ID"));
            }

            //  update active status
            boolean updatedPodStatus = productCommandService.updateProductPodStatus(
                    productId,
                    podAvailable
            );

            //  response
            if (podAvailable == updatedPodStatus) {
                logger.info("POD available status for product ID: {}, updated successfully", productId);
                return ResponseEntity.ok(Map.of("message", "Product POD available status update successful."));
            } else {
                logger.error("POD available status update failed for product ID: {}", productId);
                return ResponseEntity.ok(Map.of("message", "Product POD available status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update product images
     *   Product identified by product ID
     *
     */
//    @PutMapping("/update-images")
//    public ResponseEntity<?> updateProductImages(@RequestHeader("roles") String roles,
//                                                 @RequestParam("id") Long productId,
//                                                 @RequestPart("images") Map<String, MultipartFile> images) {
//        if (roles.equals("ADMIN")) {
//            //  images validation
//            if (images == null || images.isEmpty()) {
//                throw new IllegalArgumentException("Product images required.");
//            }
//            if (images.values().stream().anyMatch(file -> file == null || file.isEmpty()) || images.keySet().stream().anyMatch(k -> k == null || k.trim().isEmpty())) {
//                throw new IllegalArgumentException("Invalid product images");
//            }
//            if (images.size() < 3) {
//                throw new IllegalArgumentException("Minimum 3 images required.");
//            }
//            if (images.size() > 9) {
//                throw new IllegalArgumentException("You can upload a maximum of 9 images.");
//            }
//
//            //  updating product images
//            boolean imageUpdated = productCommandService.updateProductImages(productId, images);
//
//            //  response
//            if (imageUpdated) {
//                logger.info("Images updated for product ID: {}", productId);
//                return ResponseEntity.ok(Map.of("message", "Product images updated successfully."));
//            } else {
//                logger.error("Images update failed for product ID: {}", productId);
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Map.of("message", "Product images update failed."));
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }


    /*
     *
     *   Delete product (soft delete)
     *   Product identified by product ID
     *
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long productId) {
        //  input validation
        if (productId == null || productId <= 0) {
            logger.warn("Invalid product ID: {}", productId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid product ID"));
        }

        //  deleting product
        boolean deleted = productCommandService.deleteProduct(productId);

        //  response
        if (deleted) {
            logger.info("Product with ID {} deleted successfully", productId);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } else {
            logger.error("Product deletion failed for ID: {}", productId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Product deletion failed"));
        }
    }

}


/// / ******* /upload & partial search APIs *******

//    @GetMapping("/uploads/**")
//    public ResponseEntity<Resource> serveFile(HttpServletRequest request) {
//        String uri = request.getRequestURI().replace("products/uploads/", "");
//        Path file = Paths.get("uploads").resolve(uri).normalize();
//        Resource resource = new FileSystemResource(file);
//        if (!resource.exists()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
//                .body(resource);
//    }

//    @GetMapping("/partial-search")
//    public ResponseEntity<List<ProductSuggestionDto>> partialSearchProducts(@RequestParam("name") String productName) {
//        List<ProductSuggestionDto> suggestions = productsRetrievalService.partialSearchProducts(productName);
//        return ResponseEntity.ok(suggestions);
//    }


