package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Dto.ProductDto.NewProductItemDto;
import com.menzo.Product_Service.Entity.*;
import com.menzo.Product_Service.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private CategoriesService categoriesService;

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    @Autowired
    private VariationsOptionsRepo variationsOptionsRepo;

    @Autowired
    private ProductCountryOfOriginRepo countryOfOriginRepo;

    public Product addNewProduct(NewProductDto newProductDto,
                              List<MultipartFile> images) throws IOException {

        ParentCategoryDto category = categoriesRetrievalService.getParentCategoryById(newProductDto.getCategoryId());   //  fetching category details by categoryId
        SubCategoryDto subCategory = categoriesRetrievalService.getSubCategoryById(newProductDto.getSubCategoryId());
        if (subCategory == null || subCategory.getParentCategoryId() == null) {
            throw new IllegalArgumentException("Invalid sub-category: must have a parent category");
        }

        //  Add product
        Product savedProduct = saveNewProduct(newProductDto, subCategory);

        // Add photos
        List<ProductImage> savedImages = saveImages(
                category.getCategoryName(),
                subCategory.getCategoryName(),
                savedProduct,
                null,
                images
        );
        if (savedProduct == null || savedImages == null) {
            throw new IOException("Error saving product");
        }

        return savedProduct;

    }

    private Product saveNewProduct(NewProductDto newProductDto, SubCategoryDto subCategory) {

        if (productsRepo.existsByProductName(newProductDto.getProductName())) {
            throw new IllegalArgumentException("Product with product name '" + newProductDto.getProductName() + "' already exists.");
        }

        Boolean podAvailable = newProductDto.getCod().equals("available") ? true : false;
        Long countryOfOriginId = addCountryOfOrigin(newProductDto.getCountryOfOrigin());
        Long companyId = Long.valueOf(1);

        Product newProduct = new Product(
                newProductDto.getProductName(),
                new ProductCategory(subCategory),
                newProductDto.getDescription(),
                podAvailable,
                newProductDto.getItemWeight(),
                newProductDto.getGenericName(),
                countryOfOriginId,
                companyId, companyId
        );

        return productsRepo.save(newProduct);
    }

    private Long addCountryOfOrigin(String countryName) {
//        if (countryOfOriginRepo.existsByCountryNameIgnoreCase(countryName)) {
//            CountryOfOrigin countryInDB = countryOfOriginRepo.findByCountryNameIgnoreCase(countryName);
//            return countryInDB.getId();
//        } else {
//            CountryOfOrigin savedCountry = countryOfOriginRepo.save(new CountryOfOrigin(countryName));
//            return savedCountry.getId();
//        }
        return countryOfOriginRepo.findByCountryNameIgnoreCase(countryName.trim())
                .map(CountryOfOrigin::getId)
                .orElseGet(() -> {
                    CountryOfOrigin savedCountry = countryOfOriginRepo.save(new CountryOfOrigin(countryName.trim()));
                    return savedCountry.getId();
                });
    }

//    private void addManufacturerAndPacker(String manufacturer, String packer) {}

    private List<ProductImage> saveImages(String categoryName,
                                          String subCategoryName,
                                          Product savedProduct,
                                          ProductItem savedProductItem,
                                          List<MultipartFile> images) throws IOException {

        if ((savedProduct == null && savedProductItem == null) ||
                (savedProduct != null && savedProductItem != null)) {
            throw new IllegalArgumentException("Any Product or ProductItem must be provided.");
        }

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Images are mandatory.");
        }

        if (categoryName == null || subCategoryName == null) {
            throw new IllegalArgumentException("Category and Sub-category names cannot be null");
        }

        List<String> imagePaths = new ArrayList<>();

        Path uploadDir = (savedProduct != null)
                ? Paths.get("uploads", categoryName, subCategoryName)
                : Paths.get("uploads", categoryName, subCategoryName, savedProductItem.getId().toString());
        Files.createDirectories(uploadDir);

        for (MultipartFile image : images) {
            String contentType = image.getContentType();
            if (!List.of("image/png", "image/jpeg").contains(contentType)) {
                throw new IllegalArgumentException("Only PNG and JPG images are allowed.");
            }

            if (!image.isEmpty()) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                String filename = UUID.randomUUID() + "." + extension;

                Path filePath = Paths.get(String.valueOf(uploadDir), filename);
                image.transferTo(filePath);
                imagePaths.add(String.valueOf(filePath));
            }
        }
//        return imagePaths;
        List<ProductImage> imageEntities = imagePaths.stream()
                .map(path -> new ProductImage(savedProduct, savedProductItem, path))
                .collect(Collectors.toList());

        return productImagesRepo.saveAll(imageEntities);
    }

    public ProductItem addNewProductItem(NewProductItemDto newProductItemDto,
                                  Map<String, String> variationsMap,
                                  List<MultipartFile> images) throws IOException {
        Map<String, String> variations = variationsMap.entrySet().stream()
                .filter(e -> !List.of("productId", "sku", "price", "stockQty", "status").contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        newProductItemDto.setVariations(variations);

        Product product = productsRepo.findById(newProductItemDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + newProductItemDto.getProductId()));
        ProductItem savedProductItem = saveNewProductItem(product, newProductItemDto);

        ParentCategoryView parentCategoryView = categoriesRetrievalService.getParentBySubCategoryId(product.getCategory().getId());

        List<ProductImage> savedImages = saveImages(
                parentCategoryView.getCategoryName(),
                product.getCategory().getCategoryName(),
                null,
                savedProductItem,
                images
        );

        if (savedProductItem == null || savedImages == null) {
            throw new IOException("Error saving product item");
        }
        return savedProductItem;
    }


//newProduct.setCountryOfOriginId(Long.valueOf(1));

//        newProduct.setItemWeight(Float.valueOf(4.4f));

//    private ProductItem saveNewProductItem(Product savedProduct, String SKU, Integer stock,
//                Double priceValue, String activeStatus, Map<String, String> variations) {
    private ProductItem saveNewProductItem(Product product, NewProductItemDto newProductItemDto) {

        boolean isActive = newProductItemDto.getStatus().equals("active");
//        Float price = priceValue.floatValue();

        ProductItem newProductItem = new ProductItem(
                product,
                newProductItemDto.getSku(),
                newProductItemDto.getStockQty(),
                newProductItemDto.getPrice(),
                isActive);

        List<ProductConfiguration> configurationList = new ArrayList<>();
        for(Map.Entry<String, String> entry: newProductItemDto.getVariations().entrySet()) {
            Long variationOptionId = Long.valueOf(entry.getValue());
            VariationOption variationOption = variationsOptionsRepo.findById(variationOptionId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid variationOptionId: " + variationOptionId));

            ProductConfiguration configuration = new ProductConfiguration(newProductItem, variationOption);
            configurationList.add(configuration);
        }

        newProductItem.setConfigurations(configurationList);
        return productItemsRepo.save(newProductItem);
    }

    //        ProductItem savedProductItem = saveNewProductItem(savedProduct, newProductDto.getSku(), newProductDto.getStock(),
//                newProductDto.getPrice(), newProductDto.getStatus(), variations);   //  2. add product item with variations


}



