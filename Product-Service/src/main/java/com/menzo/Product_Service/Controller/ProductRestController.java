package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Dto.FilterDtos.RequestDto;
import com.menzo.Product_Service.Dto.ProductDto.ItemListingDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import com.menzo.Product_Service.Service.ProductsRetrievalService;
import com.menzo.Product_Service.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @PostMapping("all-products")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String sortRequest,
                                            @RequestBody(required = false) RequestDto requestDto) {
        System.out.println("page: " + page + "\tsize: " + size);
        System.out.println("sort: " + sortRequest);
        System.out.println("requestDto: " + requestDto);
        Page<ProductListingDto> pageContent = productsRetrievalService.getAdminAllProductListing(
                page,
                size,
                sortRequest != null ? sortRequest : "",
                requestDto != null ? requestDto : new RequestDto()
        );
        return ResponseEntity.ok(pageContent);
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(@RequestParam("id") Long productId) {
        List<ItemListingDto> items = productsRetrievalService.getAllItems(productId);
        return ResponseEntity.ok(items);
    }



    //  Get all products with pagination for listing - for Admin-Service (/admin/all-products)
//    @GetMapping("/all-products-listing")
//    public ResponseEntity<Page<ProductListingDto>> getAllProductListingWithPagination(@RequestParam(defaultValue = "0") Integer page,
//                                                                                      @RequestParam(defaultValue = "10") Integer size) {
//        Page<ProductListingDto> productListingDtos = productsRetrievalService.getAllProductListing(page, size);
//        return ResponseEntity.ok(productListingDtos);
//    }

//    @PostMapping("/all-products")
//    public ResponseEntity<?> getAdminAllProducts(@RequestParam(defaultValue = "0") Integer page,
//                                            @RequestParam(defaultValue = "10") Integer size,
//                                            @RequestParam(required = false) String sort,
//                                            @RequestBody(required = false) ProductDetailsDto p) {
//        System.out.println(sort);
//        if (sort == null) sort = "";
//        Page<ProductListingDto> pages =  productsRetrievalService.getAllProductListing(page, size, sort);
//        return ResponseEntity.ok(pages);
//    }

//    @PostMapping("/all-products-listing")
//    public ResponseEntity<Page<ProductListingDto>> getAllProductListingWithPagination(@RequestBody(required = false) RequestDto requestDto,
//                                                                                      @RequestParam(required = false) Long categoryId,
//                                                                                      @RequestParam(defaultValue = "0") Integer page,
//                                                                                      @RequestParam(defaultValue = "10") Integer size) {
//
//        Page<ProductListingDto> productListingDtos = productsRetrievalService.getAllProductListing(requestDto, categoryId, page, size);
//        return ResponseEntity.ok(productListingDtos);
//    }

//    @PostMapping("/hi")
//    public List<?> getAllProductWithFilters(@RequestBody(required = false) RequestDto requestDto) {
//        System.out.println(requestDto);
//        return productsRetrievalService.getAllProductListingWithSpec(requestDto);
//    }










    //  Get all productItems by product id, with pagination for listing - for Admin-Service
//    @GetMapping("/product-items")
//    public ResponseEntity<Page<ItemListingDto>> getAllProductItemsByProductIdWithPagination(@RequestParam("id") Long productId,
//                                                                                   @RequestParam(defaultValue = "0") Integer page,
//                                                                                   @RequestParam(defaultValue = "10") Integer size) {
//        Page<ItemListingDto> productItemsListingDtos = productsRetrievalService.getAllProductItemsByProductIdWithPagination(productId, page, size);
//        return ResponseEntity.ok(productItemsListingDtos);
//    }
//
//    //  Get product by ID - for ADMIN-SERVICE
//    @GetMapping("/get-by-id")
//    public ResponseEntity<ProductMinimalDto> getProductById(@RequestParam("id") Long productId){
//        ProductMinimalDto productDto = productsRetrievalService.getProductByIdForAddItemForm(productId);
//        return ResponseEntity.ok(productDto);
//    }
//
//    //  Get product details by ID - for ADMIN-SERVICE
//    @GetMapping("/get-details-by-id")
//    public ResponseEntity<ProductDetailsDto> getProductDetailsById(@RequestParam("id") Long productId) {
//        ProductDetailsDto productDetails = productsRetrievalService.getProductDetailsById(productId);
//        return ResponseEntity.ok(productDetails);
//    }

//    @GetMapping("/hello")
//    public ResponseEntity<?> getProductItemDetailsById(@RequestParam("id") Long itemId) {
//        Optional<ProductItem> p = productsRetrievalService.getProductItemDetailsById(itemId);
//        return ResponseEntity.ok(p);
//    }









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

//    ------------

//    @GetMapping("/partial-search")
//    public ResponseEntity<List<ProductSuggestionDto>> partialSearchProducts(@RequestParam("name") String productName) {
//        List<ProductSuggestionDto> suggestions = productsRetrievalService.partialSearchProducts(productName);
//        return ResponseEntity.ok(suggestions);
//    }

}
