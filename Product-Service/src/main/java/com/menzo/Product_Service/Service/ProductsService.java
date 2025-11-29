package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.ProductDto.*;
import com.menzo.Product_Service.Entity.*;
import com.menzo.Product_Service.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Enum.StockStatus;
import com.menzo.Product_Service.Repository.*;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ProductItemsRepo productItemsRepo;

    @Autowired
    private ProductImagesRepo productImagesRepo;

    @Autowired
    private VariationsOptionsRepo variationsOptionsRepo;

    @Autowired
    private ProductCountryOfOriginRepo countryOfOriginRepo;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    @Autowired
    private VariationsRetrievalService variationsRetrievalService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    //  TARGET_INVENTORY_LEVEL
    private static Long til;

    @Value("#{'${target-inventory-level}'}")
    public void setTil(String til) {
        this.til = Long.valueOf(til);
    }


    /*
     *   ******* Add new PRODUCT *******
     *
     *   Every PRODUCT save will have  ->  One 'color variation' & Multiple 'Size variations'
     *
     *
     *   The PRODUCT will be saved first
     *   Variations other than 'Color' & 'Size'  ->  will be associated with the PRODUCT
     *
     *
     *   For every PRODUCT  ->  multiple PRODUCT ITEMS will be created
     *   For every 'Size variation'  ->  individual PRODUCT ITEM
     *   The IMAGES will be associated with the PRODUCT ITEM
     *
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
//    @Transactional
//    public Product addNewProduct(NewProductDto newProduct,
//                                 Map<String, String> variationMap,
//                                 List<MultipartFile> images) throws IOException {
//
//        //  Fetching sub-category
//        ParentCategoryDto parentCategory = categoriesRetrievalService       // ## validate - subcategory belongs to category
//                .getParentCategoryById(newProduct.getCategoryId());
//        ProductCategory subCategory = categoriesRetrievalService
//                .getSubCategoryById(newProduct.getSubCategoryId());
//        if (parentCategory == null)
//            throw new IllegalArgumentException("Parent category cannot be null");
//        if (subCategory == null || subCategory.getParentCategoryId() == null)
//            throw new IllegalArgumentException("Invalid sub-category with ID: " +
//                    newProduct.getSubCategoryId() + " - must have a parent category");
//
//        //  Saving Product
//        Product savedProduct = saveNewProduct(
//                newProduct,
//                subCategory
//        );
//
//        // Processing Variation Options
//        List<VariationOption> variationOptionList = processVariations(
//                variationMap,
//                null
//        );
//        if (variationOptionList == null) {
//            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
//        }
//
//        //  Saving Product Item
//        List<ProductItem> savedProductItems = saveNewProductItem(
//                newProduct.getSizeStockMap(),
//                variationOptionList,
//                subCategory,
//                savedProduct,
//                newProduct.getColor(),
//                newProduct.getPrice(),
//                newProduct.getStatus().equals("active")
//        );
//        if (savedProductItems.size() != newProduct.getSizeStockMap().size())
//            throw new RuntimeException("Number of (product items input) doesn't match (saved product items)");
//
//        String superSku = savedProductItems.stream().findFirst()
//                .map(item -> item.getSuperSku())
//                .orElseThrow(() -> new RuntimeException("Saved item doesn't have superSKU"));
//
//        //  saving images
//        List<ProductImage> savedImages = saveImages(
//                parentCategory.getCategoryName(),
//                subCategory.getCategoryName(),
//                savedProduct.getId(),
//                superSku,
//                savedProductItems,
//                images
//        );
//        if (savedProduct == null
//                || savedProductItems == null
//                || savedImages == null) {
//            throw new IOException("Error saving product");
//        }
//        return savedProduct;
//    }


    /*
     *   ******* Add new PRODUCT ITEM *******
     *
     *   Every new PRODUCT ITEM saved  ->  will be associated with an existing PRODUCT
     *   Every new PRODUCT ITEM will have  ->  One 'color variation' unique to the PRODUCT ITEM & Multiple 'Size variations'
     *
     *
     *   Every new 'Add PRODUCT ITEM'  ->  will create multiple PRODUCT ITEM objects for given number of 'Size variations'
     *
     *      *** DONE ***
     */
    @Transactional
    public ItemDetailsDto addNewProductItem(NewProductItemDto newProductItem,
                                            List<SizeDetailsDto> sizeDetails,
                                            List<MultipartFile> images) throws IOException {

        //  --------- Data Pre-processing ---------
        //  getting PRODUCT, CATEGORY & SUB-CATEGORY by 'product ID'
        logger.info("Add new item: Data pre-processing");
        Product product = productsRepo.findById(newProductItem.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + newProductItem.getProductId()));

        ParentCategoryView category = categoriesRetrievalService.getParentByProductId(product.getId());
        ProductCategory subCategory = product.getCategory();

        //  getting list of PRODUCT_VARIATION_CONFIGURATION from 1st found 'productItem' of 'product'
        List<ProductConfiguration> configs = product.getItems().get(0).getConfigurations();
        if (configs == null || configs.isEmpty()) {
            throw new EntityNotFoundException("Product doesn't have any Items");
        }

        //  getting VARIATION OPTIONS - excluding 'color' & 'size'
        List<VariationOption> variationOptionList = processVariations(
                null,
                configs
        );
        if (variationOptionList == null || variationOptionList.isEmpty()) {
            throw new RuntimeException("Variations unavailable. Error while processing variationsMap.");
        }

        //  getting COLOR variation option by 'color ID'
        VariationOption color = variationsRetrievalService.getOptionByIdAndVariationName(
                newProductItem.getColorId(),
                "Colors"
        );

        //  generating SUPER SKU
        String superSku = generateSKU(
                null,
                subCategory.getAbbreviation(),
                product.getId(),
                color.getColorCode().getColorAbbreviation(),
                null
        );

        //  --------- saving PRODUCT ITEMS ---------
        AtomicReference<Float> startingPrice = new AtomicReference<>(Float.MAX_VALUE);
        AtomicInteger statusFlag = new AtomicInteger(0);
        AtomicInteger stockSum = new AtomicInteger(0);

        List<ProductItem> savedItems = new ArrayList<>();
        List<ItemSizeDto> sizeDetailDtos = new ArrayList<>();

        logger.info("Saving product items");
        for (SizeDetailsDto sizeDetail : sizeDetails) {
            ProductItem savedItem = saveNewItem(
                    product,
                    superSku,
                    sizeDetail,
                    variationOptionList,
                    color,
                    newProductItem.getStatus().equalsIgnoreCase("active")
            );
            savedItems.add(savedItem);

            if (savedItem.getPrice() < startingPrice.get()) {
                startingPrice.set(savedItem.getPrice());
            }
            if (savedItem.getIsActive()) {
                statusFlag.incrementAndGet();
            }
            stockSum.addAndGet(savedItem.getQtyInStock());

            ItemSizeDto sizeDto = ItemSizeDto.builder()
                    .itemId(savedItem.getId())
                    .size(sizeDetail.getSizeValue())
                    .sku(savedItem.getSKU())
                    .qtyInStock(savedItem.getQtyInStock())
                    .isActive(savedItem.getIsActive())
                    .createdAt(savedItem.getCreatedAt())
                    .build();

            sizeDetailDtos.add(sizeDto);
        }

        //  --------- returning ITEM DETAILS ---------
        //  get stockStatus & activeStatus
        logger.info("Deriving stock & active status");
        long itemCount = savedItems.size();
        StockStatus stockStatus = productsRetrievalService.getStockStatus(
                til,
                stockSum.get() / itemCount
        );

        ProductActiveStatus activeStatus = statusFlag.get() == itemCount
                ? ProductActiveStatus.ACTIVE
                : statusFlag.get() == 0 ? ProductActiveStatus.INACTIVE
                : ProductActiveStatus.PARTIAL;

        List<ProductImage> savedImages = saveImages(
                category.getCategoryName(),
                subCategory.getCategoryName(),
                product.getId(),
                superSku,
                savedItems,
                images
        );

        List<String> imageUrls = savedImages.stream()
                .map(image -> image.getImageUrl())
                .toList();

        logger.info("Returning item details for super SKU: {}", superSku);
        return ItemDetailsDto.builder()
                .startingPrice(startingPrice.get())
                .imageUrls(imageUrls)
                .sizeDetails(sizeDetailDtos)
                .superSku(superSku)
                .stockStatus(stockStatus)
                .activeStatus(activeStatus)
                .color(color.getOptionValue())
                .hexCode(color.getColorCode().getColorCode())
                .build();
    }


    ////    ********* Save methods *********

    //  Save new PRODUCT to DB - TESTED - ### PENDING ###
    private Product saveNewProduct(
            NewProductDto newProductDto,
            ProductCategory subCategory) {

        //  duplicate product name - validation
        if (productsRepo.existsByProductName(newProductDto.getProductName())) {
            throw new IllegalArgumentException("Product with product name '" + newProductDto.getProductName() + "' already exists.");
        }
        Boolean podAvailable = newProductDto.getPod().equals("available");
        Long countryOfOriginId = addCountryOfOrigin(newProductDto.getCountryOfOrigin());
        Long companyId = Long.valueOf(1);

        Product newProduct = Product.builder()
                .productName(newProductDto.getProductName())
                .category(subCategory)
                .productDescription(newProductDto.getDescription())
                .genericName(newProductDto.getGenericName())
                .itemWeight(newProductDto.getItemWeight())
                .manufacturerId(companyId)
                .packersId(companyId)
                .countryOfOriginId(countryOfOriginId)
                .podAvailable(podAvailable)
                .build();
        return productsRepo.save(newProduct);
    }


    //  Save PRODUCT ITEMs - *** DONE ***
    @Transactional
    private ProductItem saveNewItem(Product product,
                                    String superSku,
                                    SizeDetailsDto sizeDetail,
                                    List<VariationOption> variations,
                                    VariationOption color,
                                    Boolean isActive) {

        //  --------- Data Pre-processing ---------
        //  product object validation
        if (product == null || product.getId() == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Product or productId required.");
        }

        //  getting the 'size' by ID
        VariationOption size = variationsRetrievalService.getOptionByIdAndVariationName(
                sizeDetail.getSizeId(),
                "Size"
        );

        // generating sku - concatenating super sku with size & next sequenced item ID
        String sku = generateSKU(
                superSku,
                null,
                null,
                null,
                size.getOptionValue()
        );

        //  --------- saving Product Item ---------
        //  creating new product item object
        ProductItem item = ProductItem.builder()
                .product(product)
                .superSku(superSku)
                .SKU(sku)
                .price(sizeDetail.getSizePrice())
                .qtyInStock(sizeDetail.getSizeStock())
                .isActive(isActive)
                .build();

        ProductItem saved = productItemsRepo.save(item);

        //  creating a list of PRODUCT & VARIATION CONFIGURATION for each PRODUCT ITEM
        List<ProductConfiguration> configs = variations.stream()
                .map(opt -> {
                    return ProductConfiguration.builder()
                            .productItem(saved)
                            .variationOption(opt)
                            .build();
                }).collect(Collectors.toList());

        //  adding COLOR variation to 'configs' list
        configs.add(ProductConfiguration.builder()
                .productItem(saved)
                .variationOption(color)
                .build()
        );

        //  adding SIZE variation to 'configs' list
        configs.add(ProductConfiguration.builder()
                .productItem(saved)
                .variationOption(size)
                .build()
        );
        saved.setConfigurations(configs);

        return productItemsRepo.save(saved);
    }


    ////    ********* Utility methods *********

    //  Country of origin - management - TESTED
    public Long addCountryOfOrigin(String countryName) {
        return countryOfOriginRepo.findByCountryNameIgnoreCase(countryName.trim())
                .map(CountryOfOrigin::getId)
                .orElseGet(() -> {
                    CountryOfOrigin savedCountry = countryOfOriginRepo.save(
                            CountryOfOrigin.builder()
                                    .countryName(countryName.trim())
                                    .build()
                    );
                    return savedCountry.getId();
                });
    }


    //  Variations processing - TESTED
    //  provides the variation details of the product other than 'size' & 'color' variations
    //  ## no validation for if the optionIds in value is bound with the key data or not
    public List<VariationOption> processVariations(Map<String, String> variationsMap,
                                                   List<ProductConfiguration> productConfigs) {
        if (variationsMap != null && productConfigs == null) {
            Map<String, String> variations = variationsMap.entrySet().stream()
                    .filter(e -> !List.of(
                            "productName",
                            "description",
                            "sizeStockMap",
                            "color",
                            "status",
                            "pod",
                            "price",
                            "itemWeight",
                            "genericName",
                            "countryOfOrigin",
                            "manufacturer",
                            "packer",
                            "categoryId",
                            "subCategoryId",
                            "size",
                            "discount",
                            "discount-type"
                    ).contains(e.getKey()))
                    .filter(e -> !e.getKey().startsWith("sizeStockMap["))
                    .collect(Collectors.toMap(e -> e.getKey().split("\\.")[1], Map.Entry::getValue));       //  ## can simplify this operation by doing both filtering the map and extracting ids together

//            variations.entrySet().stream().forEach(v -> System.out.println(v));
            List<Long> idList = variations.entrySet().stream()
                    .map(e -> Long.valueOf(e.getValue()))
                    .collect(Collectors.toList());
            return variationsRetrievalService.getOptionsByIds(idList);
        } else if (variationsMap == null && productConfigs != null) {
            logger.info("Processing variations: config list");

            //  fetching the IDs of all 'size' & 'color' options available in DB
            List<Long> variationIds = new ArrayList<>(variationsRetrievalService.getOptionIdsByVariation("Size"));
            variationIds.addAll(variationsRetrievalService.getOptionIdsByVariation("Colors"));

            //  getting the variation options of the product available in the ProductConfiguration table - other than 'size' & 'color'
            return productConfigs.stream()
                    .filter(config -> !variationIds.contains(config.getVariationOption().getId()))
                    .map(config -> config.getVariationOption())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }


    //  generate sku - TESTED
    private String generateSKU(String superSku,
                               String subCategoryAbbreviation,
                               Long productId,
                               String colorAbbreviation,
                               String size) {
        if (superSku == null && size == null) {
            logger.info("Generating super SKU");
            return subCategoryAbbreviation + "-" +
                    productId.toString() + "-" +
                    colorAbbreviation;
        } else if (subCategoryAbbreviation == null && productId == null && colorAbbreviation == null) {
            logger.info("Generating SKU");
            return superSku + "-" +
                    size;
        } else return null;
    }


    //  save Images
    private List<ProductImage> saveImages(String categoryName,
                                          String subCategoryName,
                                          Long productId,
                                          String superSku,
                                          List<ProductItem> productItems,
                                          List<MultipartFile> images) throws IOException {

        //  input validation
        if (categoryName == null || subCategoryName == null) {
            throw new IllegalArgumentException("Category and Sub-category names cannot be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (superSku == null || superSku.isEmpty()) {
            throw new IllegalArgumentException("superSKU missing");
        }
        if (productItems == null || productItems.isEmpty()) {
            throw new IllegalArgumentException("Product items cannot be null");
        }
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Images are mandatory.");
        }

        List<String> imagePaths = new ArrayList<>();

        //  creating - Directory with custom path
        Path uploadDir = Paths.get("uploads", categoryName, subCategoryName, productId.toString(), superSku);
        Files.createDirectories(uploadDir);

        //  image processing
        logger.info("Saving images for super SKU: {}", superSku);
        for (MultipartFile image : images) {
            String contentType = image.getContentType();

            //  image file - sanitizing with white listed formats
            if (!List.of("image/png", "image/jpeg").contains(contentType)) {
                throw new IllegalArgumentException("Only PNG and JPG images are allowed.");
            }
            if (!image.isEmpty()) {

                //  creating - image filename
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                String filename = UUID.randomUUID() + "." + extension;

                //  image file path & storing
                Path filePath = Paths.get(String.valueOf(uploadDir), filename);
                image.transferTo(filePath);
                imagePaths.add(String.valueOf(filePath));
            }
        }
        List<ProductImage> imageEntities = imagePaths.stream()
                .map(path -> {
                    return ProductImage.builder()
                            .superSku(superSku)
                            .productItems(productItems)
                            .imageUrl(path)
                            .build();
                }).collect(Collectors.toList());
        return productImagesRepo.saveAll(imageEntities);
    }

}
