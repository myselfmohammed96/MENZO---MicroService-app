package com.menzo.Product_Service.Product.Controller;

import com.menzo.Product_Service.Product.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.UserProductDetailsDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.UserProductListingDto;
import com.menzo.Product_Service.Product.Service.ProductQueryService;
import com.menzo.Product_Service.SearchAndFilter.Dto.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(ProductQueryRestController.class);

    @Autowired
    private ProductQueryService productQueryService;


    /*
     *
     *   Get product listing for user/client side
     *   With filter, sort & search
     *
     */
    @PostMapping("/get-listing")
    public ResponseEntity<?> getUserProductListing(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(name = "search", required = false) String searchRequest,
                                                   @RequestParam(name = "sort", required = false) String sortRequest,
                                                   @RequestBody(required = false) RequestDto requestDto) {
        //  input validation
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be less than 0.");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be 0 to 100");
        }
        if (searchRequest != null && !searchRequest.isEmpty() && searchRequest.length() > 120) {
            throw new IllegalArgumentException("Search text too long.");
        }

        Page<UserProductListingDto> pageContent = productQueryService.getUserProductListing(
                page,
                size,
                searchRequest == null ? "" : searchRequest,
                sortRequest == null ? "" : sortRequest,
                requestDto == null ? new RequestDto() : requestDto
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent != null) {
            responseBody.put("message", "");
            responseBody.put("pageContent", pageContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            responseBody.put("message", "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *
     *   Get product listing for admin side
     *   With filters, sort & search
     *
     */
    @PostMapping("/get-admin-listing")
    public ResponseEntity<?> getAdminProductListing(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(name = "search", required = false) String searchRequest,
                                                    @RequestParam(name = "sort", required = false) String sortRequest,
                                                    @RequestBody(required = false) RequestDto requestDto) {
        //  validations
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be less than 0.");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be 0 to 100");
        }
        if (searchRequest != null && !searchRequest.isEmpty() && searchRequest.length() > 120) {
            throw new IllegalArgumentException("Search text too long.");
        }
        //  ## whitelist the allowable sort requests

        //  get page content
        Page<AdminProductListingDto> pageContent = productQueryService.getAdminProductListing(
                page,
                size,
                searchRequest == null ? "" : searchRequest,
                sortRequest == null ? "" : sortRequest,
                requestDto == null ? new RequestDto() : requestDto
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent != null) {
            responseBody.put("message", "");
            responseBody.put("pageContent", pageContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            responseBody.put("message", "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *
     *   Get product details for user/client side
     *   With items minimal details
     *   Product identified by super SKU
     *
     */
    @GetMapping("/get-details")
    public ResponseEntity<?> getUserProductDetails(@RequestParam("ssku") String superSku) {
        UserProductDetailsDto productDetails = productQueryService.getUserProductDetails(superSku);
        return ResponseEntity.ok(productDetails);
    }


    /*
     *
     *   Get product images
     *
     */
    @GetMapping("/get-images")
    public ResponseEntity<?> getProductImages(@RequestParam("ssku") String superSku) {
        List<String> productImages = productQueryService.getProductImages(superSku);
        return ResponseEntity.ok(productImages);
    }

}
