package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.Category.Dto.CategoryDto;
import com.menzo.Product_Service.Category.Service.CategoryQueryService;
import com.menzo.Product_Service.Feign.UserFeign;
import com.menzo.Product_Service.GlobalComponents.Dto.EmailDto;
import com.menzo.Product_Service.Product.Dto.*;
import com.menzo.Product_Service.Product.Dto.ProductDto.CreateProductDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.ProductDto;
import com.menzo.Product_Service.Product.Entity.*;

import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Product.Repo.ProductCountryOfOriginRepository;
import com.menzo.Product_Service.Product.Repo.ProductsRepository;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductCommandService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCommandService.class);

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ItemCommandService itemCommandService;

    @Autowired
    private ProductCountryOfOriginRepository countryOfOriginRepo;

    @Autowired
    private CategoryQueryService categoriesQueryService;

    @Autowired
    private OptionQueryService optionQueryService;

    @Autowired
    private ProductUtilityService productUtilityService;

    @Autowired
    private UserFeign userFeign;

    //  TARGET_INVENTORY_LEVEL
    private static Long til;

    @Value("#{'${target-inventory-level}'}")
    public void setTil(String til) {
        this.til = Long.valueOf(til);
    }


    /*
     *
     *   Add new product
     *
     *   Every PRODUCT save will have  ->  One 'color variation' & Multiple 'Size variations'
     *
     *   The PRODUCT will be saved first
     *   Variations other than 'Color' & 'Size'  ->  will be associated with the PRODUCT
     *
     *   For every PRODUCT  ->  multiple PRODUCT ITEMS will be created
     *   For every 'Size variation'  ->  individual PRODUCT ITEM
     *   The IMAGES will be associated with the PRODUCT ITEM
     *
     *   Every 'Color' has individual  ->  Price
     *                                     Active Status
     *
     *   Every 'Size' has individual  ->  PRODUCT ITEM
     *                                    Stock quantity
     *
     *   Every 'sku'  ->  Unique to the PRODUCT ITEM
     *
     */
    @Transactional
    public Long addNewProduct(CreateProductDto newProductDetails,
                              List<SizeDetailsDto> sizeDetails,
                              Map<String, String> variationDetailsMap,
                              Map<String, MultipartFile> images) throws IOException {

        //  --------- Data Pre-processing ---------
        //  getting parent category & sub-category
        CategoryDto parentCategory = categoriesQueryService
                .getParentCategoryById(newProductDetails.getCategoryId());
        ProductCategory subCategory = categoriesQueryService            //  ## validate - subcategory belongs to category
                .getSubCategoryEntityById(newProductDetails.getSubCategoryId());
        if (parentCategory == null)
            throw new IllegalArgumentException("Parent category cannot be null");
        if (subCategory == null || subCategory.getParentCategory().getCategoryId() == null)
            throw new IllegalArgumentException("Invalid sub-category with ID: " +
                    newProductDetails.getSubCategoryId() + " - must have a parent category");

        //  saving product
        Product savedProduct = saveNewProduct(
                newProductDetails,
                subCategory
        );

        //  Processing Variation Options
        List<VariationOption> variationOptions = productUtilityService.processVariations(
                variationDetailsMap,
                null
        );
        if (variationOptions == null) {
            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
        }

        //  getting COLOR variation option by 'color ID'
        VariationOption color = optionQueryService.getOptionByIdAndVariationName(
                newProductDetails.getColorId(),
                "Colors"
        );

        //  generating SUPER SKU
        String superSku = productUtilityService.generateSKU(
                null,
                subCategory.getAbbreviation(),
                savedProduct.getProductId(),
                color.getColorCode().getColorAbbreviation(),
                null
        );

        //  --------- saving PRODUCT ITEMS ---------
        List<ProductItem> savedItems = new ArrayList<>();

        logger.info("Saving product items");
        for (SizeDetailsDto sizeDetail : sizeDetails) {
            ProductItem savedItem = itemCommandService.saveNewItem(
                    savedProduct,
                    superSku,
                    sizeDetail,
                    variationOptions,
                    color,
                    newProductDetails.getActiveStatus().equalsIgnoreCase("active")
            );
            savedItems.add(savedItem);
        }
        if (savedItems.size() != sizeDetails.size()) {
            throw new RuntimeException("Number of 'product items input' doesn't match 'saved product items'");
        }

        //  saving images
        List<ProductImage> savedImages = productUtilityService.saveImages(
                parentCategory.getCategoryName(),
                subCategory.getCategoryName(),
                savedProduct.getProductId(),
                superSku,
                savedItems,
                images
        );
        if (savedProduct == null
                || savedItems == null
                || savedItems.isEmpty()
                || savedImages == null
                || savedImages.isEmpty()) {
            throw new RuntimeException("Error saving product");
        }
        return savedProduct.getProductId();
    }


    /*
     *
     *   Save new product to DB
     *
     */
    private Product saveNewProduct(CreateProductDto newProductDetails,
                                   ProductCategory subCategory) {

        //  duplicate product name - validation
        if (productsRepo.existsByProductNameAndProductCode(newProductDetails.getProductName(), newProductDetails.getProductCode())) {
            throw new IllegalArgumentException("Product with product name '" + newProductDetails.getProductName() + "' & '"
                    + newProductDetails.getProductCode() + "' already exists.");
        }

        //  data pre-processing
        boolean podAvailable = newProductDetails.getPodAvailable()
                .equalsIgnoreCase("available");
        boolean isActive = newProductDetails.getActiveStatus()
                .equalsIgnoreCase("active");
        CountryOfOrigin country = addCountryOfOrigin(newProductDetails.getCountryOfOrigin());
        long companyId = 1L;

        //  saving product
        Product newProduct = Product.builder()
                .productName(newProductDetails.getProductName())
                .productCode(newProductDetails.getProductCode())
                .subCategory(subCategory)
                .productDescription(newProductDetails.getDescription())
                .podAvailable(podAvailable)
                .isActive(isActive)
                .genericName(newProductDetails.getGenericName())
                .itemWeight(newProductDetails.getItemWeight())
                .manufacturerId(companyId)
                .packersId(companyId)
                .countryOfOrigin(country)
                .build();
        return productsRepo.save(newProduct);
    }


    /*
     *
     *   Country of origin
     *   Find by country name
     *   or
     *   Save new country of origin
     *
     */
    private CountryOfOrigin addCountryOfOrigin(String countryName) {
        return countryOfOriginRepo.findByCountryNameIgnoreCase(countryName.trim())
                .orElseGet(() -> countryOfOriginRepo.save(
                        CountryOfOrigin.builder()
                                .countryName(countryName.trim())
                                .build()
                ));
    }


    /*
     *
     *   Update product
     *   Product identified by product ID
     *
     */
    public boolean updateProductDetails(Long productId, ProductDto latestProduct) {

        //  fetching product by ID
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        //  updating product name
        product.setProductName(latestProduct.getProductName() != null
                && !latestProduct.getProductName().isEmpty()
                ? latestProduct.getProductName()
                : product.getProductName());

        //  updating product code
        product.setProductCode(latestProduct.getProductCode() != null
                && !latestProduct.getProductCode().isEmpty()
                ? latestProduct.getProductCode()
                : product.getProductCode());

        //  updating product sub-category
        if (latestProduct.getSubCategoryId() != null
                && latestProduct.getSubCategoryId() >= 0L
                && !Objects.equals(latestProduct.getSubCategoryId(), product.getSubCategory().getCategoryId())) {
            ProductCategory latestSub = categoriesQueryService.getSubCategoryEntityById(latestProduct.getSubCategoryId());
            product.setSubCategory(latestSub);
        }

        //  updating product description
        product.setProductDescription(latestProduct.getProductDescription() != null
                && !latestProduct.getProductDescription().isEmpty()
                ? latestProduct.getProductDescription()
                : product.getProductDescription());

        //  updating generic name
        product.setGenericName(latestProduct.getGenericName() != null
                && !latestProduct.getGenericName().isEmpty()
                ? latestProduct.getGenericName()
                : product.getGenericName());

        //  updating item weight
        product.setItemWeight(latestProduct.getItemWeight() != null
                && latestProduct.getItemWeight() >= 0f
                ? latestProduct.getItemWeight()
                : product.getItemWeight());

        //  updating manufacturer
        product.setManufacturerId(latestProduct.getManufacturerId() != null
                && latestProduct.getManufacturerId() >= 0l
                ? latestProduct.getManufacturerId()
                : product.getManufacturerId());

        //  updating packer
        product.setPackersId(latestProduct.getPackerId() != null
                && latestProduct.getPackerId() >= 0l
                ? latestProduct.getPackerId()
                : product.getPackersId());

        //  updating country of origin
        addCountryOfOrigin(latestProduct.getCountyOfOrigin());
        product.setCountryOfOrigin(latestProduct.getCountyOfOrigin() != null
                && !latestProduct.getCountyOfOrigin().isEmpty()
                ? addCountryOfOrigin(latestProduct.getCountyOfOrigin())
                : product.getCountryOfOrigin());

        productsRepo.save(product);
        logger.info("Product details updated for product ID: {}", productId);
        return true;
    }


    /*
     *
     *   Update product active status
     *   Product identified by product ID
     *
     */
    public boolean updateProductActiveStatus(Long productId, boolean isActive) {

        //  fetching product by ID
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        //  updating product active status
        product.setActive(isActive);
        return productsRepo.save(product).isActive();
    }


    /*
     *
     *   Update product approval status
     *   Product identified by product ID
     *   Approving staff identified by email address
     *
     */
    public boolean updateProductApprovalStatus(String staffEmail,
                                               Long productId,
                                               boolean isApproved) {
        //  fetching product by ID
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        //  fetching staff ID of approving staff
        long approvingStaffId = userFeign.getStaffIdByStaffEmail(new EmailDto(staffEmail)).getStaffId();

        //  updating product approval status
        product.setApproved(isApproved);
        product.setApprovedBy(approvingStaffId);
        return productsRepo.save(product).isApproved();
    }


    /*
     *
     *   Update product POD status
     *   product identified by product ID
     *
     */
    public boolean updateProductPodStatus(Long productId, boolean podAvailable) {

        //  fetching product by ID
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        //  updating product POD status
        product.setActive(podAvailable);
        return productsRepo.save(product).isPodAvailable();
    }


    /*
     *
     *   Update product images
     *   Product identified by product ID
     *
     */
//    public boolean updateProductImages(Long productId,
//                                       Map<String, MultipartFile> images) {
//
//        //  fetching product by ID
//        Product product = productsRepo.findById(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
//        product.get
//        return productUtilityService.updateImages(product);
//    }


    /*
     *
     *   Delete product (soft delete)
     *   Product identified by product ID
     *
     */
    public boolean deleteProduct(Long productId) {

        //  fetching product by ID
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        //  soft delete: set isDeleted to true if not already
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productsRepo.save(product);
        logger.info("Deleted product with ID: {}", productId);
        return true;
    }

}
