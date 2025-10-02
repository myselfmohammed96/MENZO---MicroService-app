package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.ProductDto.*;
import com.menzo.Product_Service.Dto.SpecificationsDto.RequestDto;
import com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto;
import com.menzo.Product_Service.Entity.*;
import com.menzo.Product_Service.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsRetrievalService {

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ProductItemsRepo productItemsRepo;

    @Autowired
    private ProductImagesRepo productImagesRepo;

    @Autowired
    private ProductConfigurationRepo productConfigurationRepo;

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    @Autowired
    private ProductCountryOfOriginRepo countryOfOriginRepo;

    @Autowired
    private ProductSpecificationService<Product> productSpecService;

    @Autowired
    private VariationsOptionsRepo variationsOptionsRepo;

    private static final Logger logger = LoggerFactory.getLogger(ProductsRetrievalService.class);

    //  ********* Get all products listing with pagination & filtering *********
    public Page<ProductListingDto> getAllProductListing(RequestDto requestDto, Long categoryId, Integer page, Integer size) {
        Page<Product> products;
        Pageable sortedPageable = PageRequest.of(
                page, size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        if (requestDto != null) {
            Specification<Product> productSpecs = productSpecService.getFilterSpecification(requestDto.getFilterRequestDtos());
            products = productsRepo.findAll(productSpecs, sortedPageable);
        } else if (requestDto == null){
            products = productsRepo.findAll(sortedPageable);
        } else {
            throw new IllegalArgumentException("Check the request dto");
        }

        System.out.println(products);
        List<ProductListingDto> productListingDtos = products.stream()
                .map(this::convertProductToProductListing)
                .collect(Collectors.toList());
        System.out.println(productListingDtos);

        return new PageImpl<>(productListingDtos, sortedPageable, products.getTotalElements());
    }


    //  ********* Get all productItems listing with pagination *********
    public Page<ProductItemListingDto> getAllProductItemsByProductIdWithPagination(Long productId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductItem> productItemsPageByProductId = productItemsRepo.findAllByProductId(productId, pageable);

        List<ProductItemListingDto> productItemListingDtos = productItemsPageByProductId.stream()
                .map(productItem -> this.convertProductItemToProductItemListingDto(productItem))
                .collect(Collectors.toList());
        return new PageImpl<>(productItemListingDtos, pageable, productItemsPageByProductId.getTotalElements());

    }


    public ProductMinimalDto getProductByIdForAddItemForm(Long productId) {
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + productId));
        ParentCategoryView parentCategoryView = categoriesRetrievalService.getParentBySubCategoryId(product.getCategory().getId());
        return new ProductMinimalDto(
                product.getId(),
                product.getProductName(),
                parentCategoryView.getId(),
                parentCategoryView.getCategoryName(),
                product.getCategory().getId(),
                product.getCategory().getCategoryName());
    }


    public ProductDetailsDto getProductDetailsById(Long productId) {
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        ParentCategoryView parentCategoryView = categoriesRetrievalService.getParentBySubCategoryId(product.getCategory().getId());
        CountryOfOrigin countryOfOrigin = countryOfOriginRepo.findById(product.getCountryOfOriginId())
                .orElseThrow(() -> new EntityNotFoundException("Country of origin not found for ID: " + product.getCountryOfOriginId()));

        List<String> images = getImagesById(productId, null);

        return new ProductDetailsDto(
                product.getId(),
                product.getProductName(),
                parentCategoryView.getCategoryName(),
                product.getCategory().getCategoryName(),
                product.getProductDescription(),
                product.getPodAvailable(),
                product.getItemWeight(),
                product.getGenericName(),
                countryOfOrigin.getCountryName(),
                "ABC private ltd, Mumbai.",
                "XYZ enterprises, Bangalore.",
                images
        );

    }

    public ProductItemDetailsDto getProductItemDetailsById(Long itemId) {
        ProductItem p = productItemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("ProductItem not found with ID: " + itemId));
        return new ProductItemDetailsDto(
                p.getId(),
                p.getProduct().getProductName(),
                null,
                p.getProduct().getCategory().getCategoryName(),
                p.getProduct().getProductDescription(),
                p.getProduct().getPodAvailable(),
                p.getProduct().getCreatedAt(),
                p.getSKU(),
                p.getQtyInStock(),
                p.getPrice(),
                null,
                null,
                p.getIsActive()
        );
    }


//    *********** Helper methods ***********

    //  converter - Product -> ProductListingDto
    private ProductListingDto convertProductToProductListing(Product product) {
        try {
            List<ProductItem> productItemsList = productItemsRepo.findAllByProductId(product.getId());
            int totalItems = 0;
            float startingPrice = Float.MAX_VALUE;
            boolean hasActive = false;
            boolean hasInactive = false;

            if (productItemsList == null) productItemsList = Collections.emptyList();
            for (ProductItem item : productItemsList) {
                if (item.getPrice() < startingPrice) {
                    startingPrice = item.getPrice();
                }
                if (Boolean.TRUE.equals(item.getIsActive())) hasActive = true;
                else hasInactive = true;

                totalItems++;
            }
            ProductActiveStatus activeStatus = hasActive && !hasInactive ? ProductActiveStatus.ACTIVE
                    : !hasActive && hasInactive ? ProductActiveStatus.INACTIVE
                    : ProductActiveStatus.PARTIALLY_ACTIVE;
            if (startingPrice == Float.MAX_VALUE) startingPrice = 0f;

            return new ProductListingDto(
                    product.getId(),
                    product.getProductName(),
                    product.getCategory().getCategoryName(),
                    startingPrice,
                    totalItems,
                    activeStatus,
                    this.getIconImage(product.getId(), null)
            );
        } catch (Exception e) {
            logger.error("Error converting product to productListingDto. Product ID: {}", product.getId(), e);
            return null;
        }
    }

    //  iconImage provider - by productId || productItemId
    private String getIconImage(Long productId, Long productItemId) {
        try {
            if (productId != null && productItemId == null) {
                List<ProductImage> productImages = productImagesRepo.findByProductId(productId);
                return productImages.isEmpty() ? null : productImages.get(0).getImageUrl();
            } else {
                List<ProductImage> productItemImages = productImagesRepo.findByProductItemId(productItemId);
                return productItemImages.isEmpty() ? null : productItemImages.get(0).getImageUrl();
            }
        } catch (Exception e) {
            logger.error("Error fetching image for product ID: {}", productId, e);
            return null;
        }
    }

    //  List<iconImage> provider - by productId || productItemid
    private List<String> getImagesById(Long productId, Long productItemId) {
        try {
            List<String> imageUrl = new ArrayList<>();

            if (productId != null && productItemId == null) {
                List<ProductImage> productImages = productImagesRepo.findByProductId(productId);
                for(ProductImage p : productImages) {
                    if (p.getImageUrl().isEmpty() || p == null) {
                        continue;
                    }
                    imageUrl.add(p.getImageUrl());
                }
            } else {
                List<ProductImage> productItemImages = productImagesRepo.findByProductItemId(productItemId);
                for(ProductImage p : productItemImages) {
                    if (p.getImageUrl().isEmpty() || p == null) {
                        continue;
                    }
                    imageUrl.add(p.getImageUrl());
                }
            }
            return imageUrl;
        } catch (Exception e) {
            logger.error("Error fetching images");
            return null;
        }
    }

    //  converter - ProductItem -> ProductItemListingDto
    private ProductItemListingDto convertProductItemToProductItemListingDto(ProductItem productItem) {
        try {
            String sizeVariationOption = this.getVariationOfProductItemByProductItemId(productItem.getId(), "Size");
            ProductActiveStatus activeStatus = productItem.getIsActive() == true ? ProductActiveStatus.ACTIVE : ProductActiveStatus.INACTIVE;

            return new ProductItemListingDto(
                    productItem.getId(),
                    productItem.getSKU(),
                    productItem.getPrice(),
                    sizeVariationOption,
                    productItem.getQtyInStock(),
                    activeStatus,
                    this.getIconImage(null, productItem.getId())
            );
        } catch (Exception e) {
            logger.error("Error converting ProductItem to ProductItemListingDto. ProductItem ID: {}", productItem.getId(), e);
            return null;
        }

    }

    //  Get variationOption by productItemId
    private String getVariationOfProductItemByProductItemId(Long productItemId, String variationName) {
        try {
            List<ProductConfiguration> configList = productConfigurationRepo.findAllByProductItemId(productItemId);
            List<OptionWithIdDto> sizeOptions = variationsRepo.findOptionsByVariationName(variationName);

            Set<Long> optionIds = configList.stream()
                    .map(config -> config.getVariationOption().getId())
                    .collect(Collectors.toSet());

            return sizeOptions.stream()
                    .filter(option -> optionIds.contains(option.getId()))
                    .map(option -> option.getOptionValue())     // .map(OptionWithIdDto::getOptionValue)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error getting Option for Variation '{}' for productItem ID: {}", variationName, productItemId, e);
            return null;
        }

    }










//    public Page<Product> getAllProductsWithPagination(Integer page, Integer size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products = productsRepo.findAll(pageable);
//        return products;
//    }
//
//    public List<ProductListingView> getAllProductListing() {
//        return productsRepo.findAllProductListing();
//    }
//
////    --------------
//
//
//    public Page<?> getAllProductListingWithPagination(Integer page, Integer size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ProductListingView> productListing = productsRepo.findAllProductListing(pageable);
//        Page<Product> products = productsRepo.findAll(pageable);
//        return products;
//    }
//
//    public List<ProductSuggestionDto> partialSearchProducts(String productName) {
//        List<Product> results = productsRepo.findByProductNameContainingIgnoreCase(productName);
//        List<ProductSuggestionDto> suggestions = results.stream()
//                .map(product -> new ProductSuggestionDto(product.getId(), product.getProductName()))
//                .toList();
//        return suggestions;
//    }



//    public List<?> getAllProductListingWithSpec(RequestDto requestDto) {
//        Specification<Product> productSpecifications = productSpecService.getFilterSpecification(requestDto.getFilterRequestDtos());
//        return productsRepo.findAll(productSpecifications);
//    }

}
