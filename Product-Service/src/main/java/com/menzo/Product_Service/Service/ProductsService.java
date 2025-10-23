//package com.menzo.Product_Service.Service;
//
//import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
//import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
//import com.menzo.Product_Service.Dto.ProductDto.ProductItemDto;
//import com.menzo.Product_Service.Entity.*;
//import com.menzo.Product_Service.Repository.*;
//import jakarta.persistence.EntityNotFoundException;
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ProductsService {
//
//    @Autowired
//    private ProductsRepo productsRepo;
//
//    @Autowired
//    private ProductItemsRepo productItemsRepo;
//
//    @Autowired
//    private ProductImagesRepo productImagesRepo;
//
//    @Autowired
//    private VariationsOptionsRepo variationsOptionsRepo;
//
//    @Autowired
//    private ProductCountryOfOriginRepo countryOfOriginRepo;
//
//    @Autowired
//    private CategoriesService categoriesService;
//
//    @Autowired
//    private CategoriesRetrievalService categoriesRetrievalService;
//
//    @Autowired
//    private VariationsRetrievalService variationsRetrievalService;
//
//    @Autowired
//    private UtilityService utilityService;
//
//
//
////    public Product addNewProduct(NewProductDto newProductDto,
////                              List<MultipartFile> images) throws IOException {
////
////        ParentCategoryDto category = categoriesRetrievalService.getParentCategoryById(newProductDto.getCategoryId());   //  fetching category details by categoryId
////        SubCategoryDto subCategory = categoriesRetrievalService.getSubCategoryById(newProductDto.getSubCategoryId());
////        if (subCategory == null || subCategory.getParentCategoryId() == null) {
////            throw new IllegalArgumentException("Invalid sub-category: must have a parent category");
////        }
////
////        //  Add product
////        Product savedProduct = saveNewProduct(newProductDto, subCategory);
////
////        // Add photos
////        List<ProductImage> savedImages = saveImages(
////                category.getCategoryName(),
////                subCategory.getCategoryName(),
////                savedProduct,
////                null,
////                images
////        );
////        if (savedProduct == null || savedImages == null) {
////            throw new IOException("Error saving product");
////        }
////
////        return savedProduct;
////
////    }
//
//    /*
//    *   ******* Add new PRODUCT *******
//    *
//    *   Every PRODUCT save will have  ->  One 'color variation' & Multiple 'Size variations'
//    *
//    *
//    *   The PRODUCT will be saved first
//    *   Variations other than 'Color' & 'Size'  ->  will be associated with the PRODUCT
//    *
//    *
//    *   For every PRODUCT  ->  multiple PRODUCT ITEMS will be created
//    *   For every 'Size variation'  ->  individual PRODUCT ITEM
//    *   The IMAGES will be associated with the PRODUCT ITEM
//    *
//    *
//    *   Every 'Color' has individual  ->  Price
//    *                                     Active Status
//    *
//    *   Every 'Size' has individual  ->  PRODUCT ITEM
//    *                                    Stock quantity
//    *
//    *   Every 'sku'  ->  Unique to the PRODUCT ITEM
//    *
//    */
////    public void addNewProductV2(NewProductDto newProduct,
////                                Map<String, String> variationMap,
////                                List<MultipartFile> image) {
////        //  Fetching sub-category
////        SubCategoryDto subCategory = categoriesRetrievalService.getSubCategoryById(newProduct.getSubCategoryId());
////        if(subCategory == null || subCategory.getParentCategoryId() == null) {
////            throw new IllegalArgumentException("Invalid sub-category with ID: " + newProduct.getSubCategoryId() + " - must have a parent category");
////        }
////        //  Saving Product
////        Product savedProduct = saveNewProduct(newProduct, subCategory);
////
////        // Processing Variation Options
////        List<VariationOption> variationOptionList = processVariations(variationMap, null);
////        if (variationOptionList == null) {
////            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
////        }
////
////        //  Saving Product Item
////        List<ProductItem> savedProductItems = saveNewProductItem(
////                newProduct.getSizeStockMap(),
////                variationOptionList,
////                subCategory,
////                new ProductItemDto(
////                        null,
////                        savedProduct,
////                        newProduct.getColor(),
////                        newProduct.getPrice(),
////                        newProduct.getStatus().equals("active")
////                )
////        );
////
////        saveImages()
////    }
//
//
//    //  Save new PRODUCT to DB
////    private Product saveNewProduct(NewProductDto newProductDto, SubCategoryDto subCategory) {
////        if (productsRepo.existsByProductName(newProductDto.getProductName())) {
////            throw new IllegalArgumentException("Product with product name '" + newProductDto.getProductName() + "' already exists.");
////        }
////        Boolean podAvailable = newProductDto.getPod().equals("available") ? true : false;
////        Long countryOfOriginId = addCountryOfOrigin(newProductDto.getCountryOfOrigin());
////        Long companyId = Long.valueOf(1);
////
////        Product newProduct = new Product(
////                newProductDto.getProductName(),
////                new ProductCategory(subCategory),
////                newProductDto.getDescription(),
////                podAvailable,
////                newProductDto.getItemWeight(),
////                newProductDto.getGenericName(),
////                countryOfOriginId,
////                companyId,
////                companyId
////        );
////        return productsRepo.save(newProduct);
////    }
//
//
//
//    /*
//     *   ******* Add new PRODUCT ITEM *******
//     *
//     *   Every new PRODUCT ITEM saved  ->  will be associated with an existing PRODUCT
//     *   Every new PRODUCT ITEM will have  ->  One 'color variation' unique to the PRODUCT ITEM & Multiple 'Size variations'
//     *
//     *
//     *   Every new 'Add PRODUCT ITEM'  ->  will create multiple PRODUCT ITEM objects for given number of 'Size variations'
//     *
//     */
////    public void addNewProductItem(ProductItemDto newProductItemDto,
////                                  Map<Long, Integer> sizeStockMap,
////                                  List<MultipartFile> images) {
////        //  Product object processing
////        Product product = productsRepo.findById(newProductItemDto.getProductId())
////                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + newProductItemDto.getProductId()));
////        newProductItemDto.setProduct(product);
////
////        //  Sub category of product
////        ProductCategory subCategory = product.getCategory();
////
////        //  Processing Variation Options
////        List<VariationOption> variationOptionList = processVariations(
////                null,
////                product.getItems().stream()
////                        .findFirst()
////                        .orElseThrow(() -> new EntityNotFoundException("No Product items found in product with ID: " + product.getId()))
////                        .getConfigurations()
////        );
////        if (variationOptionList == null) {
////            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
////        }
////
////        List<ProductItem> saveProductItems = saveNewProductItem(
////                sizeStockMap,
////                variationOptionList,
////                new SubCategoryDto(
////                        subCategory.getId(),
////                        subCategory.getParentCategoryId(),
////                        subCategory.getCategoryName(),
////                        subCategory.getIsActive(),
////                        subCategory.getCreatedAt()
////                ),
////                newProductItemDto
////        );
////    }
//
//
//    //  Save multiple new PRODUCT ITEMs to DB
////    private List<ProductItem> saveNewProductItem(Map<Long, Integer> sizeStockMap,
////                                                 List<VariationOption> variations,
////                                                 SubCategoryDto subCategory,
////                                                 ProductItemDto productItemDto) {
////        List<ProductItem> itemsList = new ArrayList<>();
////        VariationOption color = variationsRetrievalService.getOptionById(productItemDto.getColorId());
////
////        if (productItemDto.getProduct() == null) {
////            throw new IllegalArgumentException("Product or productId required.");
////        }
////
////        //  Looping size & stock map  ->  each loop creates one PRODUCT ITEM
////        for(Map.Entry<Long, Integer> e : sizeStockMap.entrySet()) {
////            VariationOption size = variationsRetrievalService.getOptionById(e.getKey());
////
////            ProductItem item = new ProductItem(
////                    productItemDto.getProduct(),
////                    generateSku(
////                            subCategory.getCategoryName(),
////                            color.getOptionValue(),
////                            size.getOptionValue(),
////                            null
////                    ),
////                    e.getValue(),
////                    productItemDto.getPrice(),
////                    productItemDto.isActive()
////            );
////
////            //  Creating a list of PRODUCT & VARIATION CONFIGURATION for each PRODUCT ITEM
////            List<ProductConfiguration> config = new ArrayList<>();
////            config.add(new ProductConfiguration(item, color));   //color
////            config.add(new ProductConfiguration(item, size));   // size
////            config = variations.stream().map(opt -> new ProductConfiguration(item, opt))
////                    .collect(Collectors.toList());
////            item.setConfigurations(config);
////            itemsList.add(item);
////        }
////        List<ProductItem> savedItems = productItemsRepo.saveAll(itemsList);
////        return savedItems;
////    }
//
//
//
////    ********* Utility methods *********
//
//    //  Generate SKU
////    private String generateSku(String subCategory, String color, String size, Long itemId) {
////
////        utilityService.
////
////
////
////
////        return subCategory.toUpperCase() + "-" + color.toUpperCase() + size.toUpperCase() + itemId;
////    }
//
//
//    //  Country of origin - management
//    private Long addCountryOfOrigin(String countryName) {
//        return countryOfOriginRepo.findByCountryNameIgnoreCase(countryName.trim())
//                .map(CountryOfOrigin::getId)
//                .orElseGet(() -> {
//                    CountryOfOrigin savedCountry = countryOfOriginRepo.save(new CountryOfOrigin(countryName.trim()));
//                    return savedCountry.getId();
//                });
//    }
//
//
//    //  Variations processing
//    private List<VariationOption> processVariations(Map<String, String> variationsMap,
//                                         List<ProductConfiguration> productConfig) {
//        if (variationsMap != null && productConfig == null) {
//            Map<String, String> variations = variationsMap.entrySet().stream()
//                    .filter(e -> !List.of(
//                            "productName",
//                            "description",
//                            "sizeStockMap",
//                            "color",
//                            "status",
//                            "pod",
//                            "price",
//                            "itemWeight",
//                            "genericName",
//                            "countryOfOrigin",
//                            "manufacturer",
//                            "packer",
//                            "categoryId",
//                            "subCategoryId"
//                    ).contains(e.getKey()))
//                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//            List<Long> idList = variations.entrySet().stream()
//                    .map(e -> Long.valueOf(e.getValue()))
//                    .collect(Collectors.toList());
//
//            return variationsRetrievalService.getOptionsByIds(idList);
//        } else if(variationsMap == null && productConfig != null) {
//            List<Long> variationIds = new ArrayList<>(variationsRetrievalService.getOptionIdsByVariation("Size"));
//            variationIds.addAll(variationsRetrievalService.getOptionIdsByVariation("Colors"));
//
//            List<Long> idList = productConfig.stream()
//                    .filter(config -> !variationIds.contains(config.getVariationOption().getId()))
//                    .map(config -> config.getVariationOption().getId())
//                    .collect(Collectors.toList());
//            return variationsRetrievalService.getOptionsByIds(idList);
//        } else {
//            return null;
//        }
//    }
//
//
//    // save Images
//    private List<ProductImage> saveImages(String categoryName,              //  ### superSku pending
//                                          String subCategoryName,
//                                          String superSku,
//                                          ProductItem savedProductItem,
//                                          List<MultipartFile> images) throws IOException {
//        if (categoryName == null || subCategoryName == null) {
//            throw new IllegalArgumentException("Category and Sub-category names cannot be null");
//        }
//        if (superSku == null || superSku.isEmpty()) {
//            throw new IllegalArgumentException("superSKU missing");
//        }
//        if (savedProductItem == null) {
//            throw new IllegalArgumentException("Product item cannot be null");
//        }
//        if (images == null || images.isEmpty()) {
//            throw new IllegalArgumentException("Images are mandatory.");
//        }
//
//        List<String> imagePaths = new ArrayList<>();
//
//        Path uploadDir = Paths.get("uploads", categoryName, subCategoryName, superSku, savedProductItem.getId().toString());
//        Files.createDirectories(uploadDir);
//
//        for (MultipartFile image : images) {
//            String contentType = image.getContentType();
//            if (!List.of("image/png", "image/jpeg").contains(contentType)) {
//                throw new IllegalArgumentException("Only PNG and JPG images are allowed.");
//            }
//            if (!image.isEmpty()) {
//                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
//                String filename = UUID.randomUUID() + "." + extension;
//
//                Path filePath = Paths.get(String.valueOf(uploadDir), filename);
//                image.transferTo(filePath);
//                imagePaths.add(String.valueOf(filePath));
//            }
//        }
//        List<ProductImage> imageEntities = imagePaths.stream()
//                .map(path -> new ProductImage(
//                        superSku,
//                        savedProductItem,
//                        path
//                )).collect(Collectors.toList());
//        return productImagesRepo.saveAll(imageEntities);
//    }
//
//}
