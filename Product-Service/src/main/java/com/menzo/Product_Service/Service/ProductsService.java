package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductItemDto;
import com.menzo.Product_Service.Entity.*;
import com.menzo.Product_Service.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService {

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
    @Transactional
    public Product addNewProduct(NewProductDto newProduct,
                                 Map<String, String> variationMap,
                                 List<MultipartFile> images) throws IOException {

        //  Fetching sub-category
        ParentCategoryDto parentCategory = categoriesRetrievalService
                .getParentCategoryById(newProduct.getCategoryId());
        ProductCategory subCategory = categoriesRetrievalService
                .getSubCategoryById(newProduct.getSubCategoryId());
        if (parentCategory == null) throw new IllegalArgumentException("Parent category cannot be null");
        if (subCategory == null || subCategory.getParentCategoryId() == null)
            throw new IllegalArgumentException("Invalid sub-category with ID: " +
                    newProduct.getSubCategoryId() + " - must have a parent category");

        //  Saving Product
        Product savedProduct = saveNewProduct(
                newProduct,
                subCategory
        );

        // Processing Variation Options
        List<VariationOption> variationOptionList = processVariations(variationMap, null);
        if (variationOptionList == null) {
            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
        }

        //  Saving Product Item
        List<ProductItem> savedProductItems = saveNewProductItem(
                newProduct.getSizeStockMap(),
                variationOptionList,
                subCategory,
                savedProduct,
                newProduct.getColor(),
                newProduct.getPrice(),
                newProduct.getStatus().equals("active")
        );
        if (savedProductItems.size() != newProduct.getSizeStockMap().size())
            throw new RuntimeException("Number of (product items input) doesn't match (saved product items)");

        //  saving images
        List<ProductImage> savedImages = saveImages(
                parentCategory.getCategoryName(),
                subCategory.getCategoryName(),
                savedProduct.getId(),
                null,
                savedProductItems,
                images
        );
        if (savedProduct == null
                || savedProductItems == null
                || savedImages == null) {
            throw new IOException("Error saving product");
        }
        return savedProduct;
    }


    /*
     *   ******* Add new PRODUCT ITEM *******
     *
     *   Every new PRODUCT ITEM saved  ->  will be associated with an existing PRODUCT
     *   Every new PRODUCT ITEM will have  ->  One 'color variation' unique to the PRODUCT ITEM & Multiple 'Size variations'
     *
     *
     *   Every new 'Add PRODUCT ITEM'  ->  will create multiple PRODUCT ITEM objects for given number of 'Size variations'
     *
     */
    public void addNewProductItem(ProductItemDto newProductItemDto,
                                  Map<Long, Integer> sizeStockMap,
                                  List<MultipartFile> images) {

        //  Product object processing
        Product product = productsRepo.findById(newProductItemDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + newProductItemDto.getProductId()));
        newProductItemDto.setProduct(product);

        //  Sub category of product
        ProductCategory subCategory = product.getCategory();

        //  Processing Variation Options
        List<VariationOption> variationOptionList = processVariations(
                null,
                product.getItems().stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No Product items found in product with ID: " + product.getId()))
                        .getConfigurations()
        );
        if (variationOptionList == null) {
            throw new RuntimeException("Variations list is null. Error while processing variationsMap.");
        }

        //  saving ProductItems
        List<ProductItem> saveProductItems = saveNewProductItem(
                sizeStockMap,
                variationOptionList,
                subCategory,
                product,
                newProductItemDto.getColorId(),
                newProductItemDto.getPrice(),
                newProductItemDto.isActive()
        );

    }


    //  Save new PRODUCT to DB - TESTED
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


    //  Save multiple new PRODUCT ITEMs to DB - TESTED
    @Transactional
    private List<ProductItem> saveNewProductItem(Map<Long, Integer> sizeStockMap,
                                                 List<VariationOption> variations,
                                                 ProductCategory subCategory,
                                                 Product product,
                                                 Long colorId,
                                                 Float price,
                                                 Boolean isActive) {
        List<ProductItem> itemsList = new ArrayList<>();

        //  fetching color option by color ID
        VariationOption color = variationsRetrievalService.getOptionById(colorId);

        //  product object validation
        if (product == null || product.getId() == null || product.getId() <= 0)
            throw new IllegalArgumentException("Product or productId required.");

        //  generating super sku
        String superSku = generateSKU(
                null,
                subCategory.getAbbreviation(),
                product.getId(),
                color.getColorCode().getColorAbbreviation(),
                null,
                null
        );

        //  Looping size & stock map  ->  each loop creates one new PRODUCT ITEM with an individual size
        for (Map.Entry<Long, Integer> e : sizeStockMap.entrySet()) {
            VariationOption size = variationsRetrievalService.getOptionById(e.getKey());

            // generating sku - concatenating super sku with size & next sequenced item ID
            Long nextId = productItemsRepo.getNextItemId();
            String sku = generateSKU(
                    superSku,
                    null,
                    null,
                    null,
                    size.getOptionValue(),
                    nextId
            );

            //  creating new product item object
            ProductItem item = ProductItem.builder()
                    .id(nextId)
                    .product(product)
                    .superSKU(superSku)
                    .SKU(sku)
                    .price(price)
                    .qtyInStock(e.getValue())
                    .isActive(isActive)
                    .build();

            //  Creating a list of PRODUCT & VARIATION CONFIGURATION for each PRODUCT ITEM
            List<ProductConfiguration> config = variations.stream()
                    .map(opt -> {
                        return ProductConfiguration.builder()
                                .productItem(item)
                                .variationOption(opt)
                                .build();
                    })
                    .collect(Collectors.toList());

            //  adding 'color' variation to 'config' list
            config.add(ProductConfiguration.builder()
                    .productItem(item)
                    .variationOption(color)
                    .build()
            );

            //  adding 'size' variation to 'config' list
            config.add(ProductConfiguration.builder()
                    .productItem(item)
                    .variationOption(size)
                    .build()
            );
            item.setConfigurations(config);
            ProductItem savedItem = productItemsRepo.save(item);
            itemsList.add(savedItem);
        }
        return itemsList;
    }


    /// /    ********* Utility methods *********

    //  Country of origin - management - TESTED
    private Long addCountryOfOrigin(String countryName) {
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
    private List<VariationOption> processVariations(Map<String, String> variationsMap,
                                                    List<ProductConfiguration> productConfig) {
        if (variationsMap != null && productConfig == null) {
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
                            "subCategoryId"
                    ).contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));     //  ## can simplify this operation by doing both filtering the map and extracting ids together

            List<Long> idList = variations.entrySet().stream()
                    .map(e -> Long.valueOf(e.getValue()))
                    .collect(Collectors.toList());
            return variationsRetrievalService.getOptionsByIds(idList);
        } else if (variationsMap == null && productConfig != null) {

            //  fetching the IDs of all 'size' & 'color' options available in DB
            List<Long> variationIds = new ArrayList<>(variationsRetrievalService.getOptionIdsByVariation("Size"));
            variationIds.addAll(variationsRetrievalService.getOptionIdsByVariation("Colors"));

            //  getting the variation options of the product available in the ProductConfiguration table - other than 'size' & 'color'
            return productConfig.stream()
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
                               String size,
                               Long productItemId) {
        if (superSku == null && size == null && productItemId == null) {
            return subCategoryAbbreviation + "-" +
                    productId.toString() + "-" +
                    colorAbbreviation;
        } else if (subCategoryAbbreviation == null && productId == null && colorAbbreviation == null) {
            return superSku + "-" +
                    size + "-" +
                    productItemId.toString();
        } else return null;
    }


    //  save Images
    private List<ProductImage> saveImages(String categoryName,              //  ### superSku pending
                                          String subCategoryName,
                                          Long productId,
                                          String superSku,
                                          List<ProductItem> productItems,
                                          List<MultipartFile> images) throws IOException {

        //  input validation
        if (categoryName == null || subCategoryName == null)
            throw new IllegalArgumentException("Category and Sub-category names cannot be null");
        if (productId == null) throw new IllegalArgumentException("Product ID cannot be null");
        if (superSku == null || superSku.isEmpty()) throw new IllegalArgumentException("superSKU missing");
        if (productItems == null || productItems.isEmpty())
            throw new IllegalArgumentException("Product items cannot be null");
        if (images == null || images.isEmpty()) throw new IllegalArgumentException("Images are mandatory.");

        List<String> imagePaths = new ArrayList<>();

        //  creating - Directory with custom path
        Path uploadDir = Paths.get("uploads", categoryName, subCategoryName, productId.toString(), superSku);
        Files.createDirectories(uploadDir);

        //  image processing
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
