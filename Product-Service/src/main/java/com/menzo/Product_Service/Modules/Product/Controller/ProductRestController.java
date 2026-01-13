package com.menzo.Product_Service.Modules.Product.Controller;

import com.menzo.Product_Service.Modules.Product.Service.ProductsQueryService;
import com.menzo.Product_Service.Modules.Product.Service.ProductsService;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.RequestDto;
import com.menzo.Product_Service.Modules.Product.Dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/products")
public class ProductRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsQueryService productsRetrievalService;


    /*
     *  ----------------------------------------
     *  ********* Admin side endpoints *********
     *  ----------------------------------------
     */

    /// /   ********* GET APIs *********

    //  Get - product listing with filters, sort
    @PostMapping("all-products")
    public ResponseEntity<?> getAdminProductListing(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) String sortRequest,
                                                    @RequestBody(required = false) RequestDto requestDto) {

        Page<AdminProductListingDto> pageContent = productsRetrievalService.getAdminProductListing(
                page,
                size,
                sortRequest != null ? sortRequest : "",
                requestDto != null ? requestDto : new RequestDto()
        );
        return ResponseEntity.ok(pageContent);
    }


    //  Get - all product items with product ID
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(@RequestParam("id") Long productId) {
        AdminProductDetailsDto productDetails = productsRetrievalService.getProductDetailsWithAllItems(productId);
        return ResponseEntity.ok(productDetails);
    }


    //  Get - product item details with super SKU
    @GetMapping("/item")
    public ResponseEntity<?> getItem(@RequestParam("ssku") String superSku) {
        ItemDetailsDto itemDetails = productsRetrievalService.getItemDetails(superSku);
        return ResponseEntity.ok(itemDetails);
    }


    /// /  ********* POST, PUT, PATCH, DELETE APIs *********

    //  add new product
    @PostMapping(
            value = "/add-product",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> addNewProduct(@RequestPart("productDetails") NewProductDto productDetails,
                                        @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                        @RequestPart("variationDetails") Map<String, String> variationDetailsMap,
                                        @RequestPart("images") List<MultipartFile> images) throws IOException {
        //  product details validation
        if (productDetails == null) {
            throw new IllegalArgumentException("Product details not found.");
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
        if (images.size() < 3) {
            throw new IllegalArgumentException("Minimum 3 images required.");
        }
        if (images.size() > 9) {
            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        }

        System.out.println(productDetails);
        System.out.println(sizeDetails);
        System.out.println(variationDetailsMap);
        images.stream().forEach(image -> System.out.println(image.getOriginalFilename()));

//        Long savedProductId = productsService.addNewProduct(
//                productDetails,
//                sizeDetails,
//                variationDetailsMap,
//                images
//        );
        Long savedProductId = 82L;

        System.out.println("saved Product ID: " + savedProductId);

        //  building response
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


    //  Add new PRODUCT ITEM by 'product ID' - *** DONE ***
    @PostMapping(
            value = "/add-item",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> addProductItem(@RequestPart("newItem") NewProductItemDto newProductItem,
                                                              @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                                              @RequestPart("images") List<MultipartFile> images) throws IOException {
        //  validations
        if (images.size() > 9) {
            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        }
        System.out.println(newProductItem);
        System.out.println(sizeDetails);
        System.out.println(newProductItem.getColorId());

        //  forwarding to service layer
        ItemDetailsDto itemDetails = productsService.addNewProductItem(
                newProductItem,
                sizeDetails,
                images
        );
        System.out.println("Saved item:\n" + itemDetails);

        //  building response
        Map<String, Object> responseBody = new HashMap<>();
        if (itemDetails != null) {
            logger.info("Product item saved successfully with super SKU: {}", itemDetails.getSuperSku());
            responseBody.put("message", "Product item saved successfully");
            responseBody.put("itemDetails", itemDetails);
            System.out.println(responseBody);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseBody);
        } else {
            logger.warn("Product item saving failed");
            responseBody.put("message", "Product item saving failed");
            System.out.println(responseBody);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *  -----------------------------------------
     *  ********* CLIENT side endpoints *********
     *  -----------------------------------------
     */

    //  product listing with filter, sort
    @PostMapping("/user-listing")
    public ResponseEntity<?> getClientProductListing(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                     @RequestParam(name = "sort", required = false) String sortRequest,
                                                     @RequestBody(required = false) RequestDto requestDto) {
        System.out.println("page: " + page + "\tsize: " + size);
        System.out.println("sort: " + sortRequest);
        System.out.println("requestDto: " + requestDto);

        Page<UserProductListingDto> pageContent = productsRetrievalService.getClientProductListing(
                page,
                size,
                sortRequest != null ? sortRequest : "",
                requestDto != null ? requestDto : new RequestDto()
        );
        return ResponseEntity.ok(pageContent);
    }


    //  get product details
    @GetMapping("/get-user-product-details")
    public ResponseEntity<?> getProductDetails(@RequestParam("ssku") String superSku) {
        UserProductDetailsDto productDetails = productsRetrievalService.getUserProductDetails(superSku);
        return ResponseEntity.ok(productDetails);
    }


    /// /   ********* IMAGES *********

    @GetMapping("/image-urls")
    public ResponseEntity<?> getProductImages(@RequestParam("ssku") String superSku) {
        List<String> productImages = productsRetrievalService.getProductImages(superSku);
        return ResponseEntity.ok(productImages);
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


