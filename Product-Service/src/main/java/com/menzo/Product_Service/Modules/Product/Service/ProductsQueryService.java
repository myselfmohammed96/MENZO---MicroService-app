package com.menzo.Product_Service.Modules.Product.Service;

import com.menzo.Product_Service.Modules.Category.Service.CategoriesRetrievalService;
import com.menzo.Product_Service.Modules.Category.Dto.ParentCategoryView;
import com.menzo.Product_Service.Modules.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Modules.Product.Entity.*;
import com.menzo.Product_Service.Modules.Product.Repo.ProductsRepo;
import com.menzo.Product_Service.Modules.Product.Repo.ProductConfigurationRepo;
import com.menzo.Product_Service.Modules.Product.Repo.ProductCountryOfOriginRepo;
import com.menzo.Product_Service.Modules.Product.Repo.ProductImagesRepo;
import com.menzo.Product_Service.Modules.Product.Repo.ProductItemsRepo;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.QueryDetailsDto;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.RequestDto;
import com.menzo.Product_Service.Modules.Product.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Modules.Product.Enum.StockStatus;
import com.menzo.Product_Service.Modules.Product.Dto.*;
import com.menzo.Product_Service.Modules.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Modules.Variation.Repo.VariationsOptionsRepo;
import com.menzo.Product_Service.Modules.Variation.Repo.VariationsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
//@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@PropertySource("classpath:metaConfig.properties")
public class ProductsQueryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductsQueryService.class);

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ProductItemsRepo itemsRepo;

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
    private VariationsOptionsRepo variationsOptionsRepo;

    //  assuming this til as the average til of every product item with common superSku
    private static Long til;

    @Value("#{'${target-inventory-level}'}")
    public void setTil(String til) {
        this.til = Long.valueOf(til);
    }


    /// /   ********* Product listing methods - with PAGINATION & FILTERING *********

    //  ADMIN side - product listing
    public Page<AdminProductListingDto> getAdminProductListing(Integer page,
                                                               Integer size,
                                                               String sortRequest,
                                                               RequestDto filterRequest) {
        String sortParam = "";
        if (sortRequest != null && !sortRequest.isEmpty()) {
            sortParam = getSortValue(sortRequest);
        }
//        Map<String, List<?>> filterValues = getFilterValues(filterRequest.getFilterRequestDtos());

        Map<String, Integer> statusFlags = new HashMap<>();
        statusFlags.put("isSubCategoryDeleted", 0);
        statusFlags.put("isCategoryDeleted", 0);
        statusFlags.put("isSubCategoryActive", 1);
        statusFlags.put("isCategoryActive", 1);

        QueryDetailsDto queryDetails = QueryDetailsDto.builder()
                .page(page)
                .size(size)
                .sortRequest(sortParam)
                .filterValues(filterRequest.getFilterRequestDtos())
                .statusFlags(statusFlags)
                .build();

        Page<AdminProductListingDto> pageContent = productsRepo.findAdminProductListing(queryDetails);

        for (AdminProductListingDto p : pageContent.getContent()) {
            System.out.println(p);
        }

        return pageContent;

        //  creating 'pageable' object with sorting
//        Sort.Order order = generateSortOrder(sortRequest);
//        Pageable sortedPageable = PageRequest.of(
//                page,
//                size,
//                Sort.by(order)
//        );
//        System.out.println(sortedPageable);

        //  creating 'specification' object

//        Page<Product> products = productsRepo.findAll(sortedPageable);
//        return convertToDto(products);
//        return null;
    }

    //  USER side - product listing
    public Page<UserProductListingDto> getClientProductListing(Integer page,
                                                               Integer size,
                                                               String sortRequest,
                                                               RequestDto filterRequest) {
        String sortParam = "";
        if (sortRequest != null && !sortRequest.isEmpty()) {
            sortParam = getSortValue(sortRequest);
        }

        Map<String, Integer> statusFlags = new HashMap<>();
        statusFlags.put("isItemActive", 1);
        statusFlags.put("podAvailable", 1);
        statusFlags.put("isCategoryDeleted", 0);
        statusFlags.put("isSubCategoryDeleted", 0);
        statusFlags.put("isCategoryActive", 1);
        statusFlags.put("isSubCategoryActive", 1);

        QueryDetailsDto queryDetails = QueryDetailsDto.builder()
                .page(page)
                .size(size)
                .sortRequest(sortParam)
                .filterValues(filterRequest.getFilterRequestDtos())
                .statusFlags(statusFlags)
                .build();

        Page<UserProductListingDto> pageContent = productsRepo.findUserProductListing(queryDetails);

        for (UserProductListingDto p : pageContent.getContent()) {
            System.out.println(p);
        }

        return pageContent;
    }


    /// /   ********* Product details methods *********

    //  ADMIN side - product details - with items minimal details
    public AdminProductDetailsDto getProductDetailsWithAllItems(Long productId) {

        //  getting PRODUCT, CATEGORY, SUB-CATEGORY & COUNTRY OF ORIGIN
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        ProductCategory subCategory = product.getCategory();
        ParentCategoryView category = categoriesRetrievalService.getParentBySubCategoryId(subCategory.getId());
        CountryOfOrigin countryOfOrigin = countryOfOriginRepo.findById(product.getCountryOfOriginId())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with ID: " + product.getCountryOfOriginId()));

        //  ITEMs - minimal details
        List<AdminItemListingDto> itemDetailsList = getMinimalItemsDetails(
                product,
                "ADMIN"
        );
        if (itemDetailsList == null || itemDetailsList.isEmpty()) {
            throw new RuntimeException("Item minimal details not found");
        }

        //  returning PRODUCT DETAILS - with ITEMs minimal details - for ITEMs listing
        return AdminProductDetailsDto.builder()
                .productName(product.getProductName())
                .categoryName(category.getCategoryName())
                .subCategoryName(subCategory.getCategoryName())
                .description(product.getProductDescription())
                .pod(product.getPodAvailable())
                .productCreated(product.getCreatedAt())
                .itemWeight(product.getItemWeight())
                .genericName(product.getGenericName())
                .countryOfOrigin(countryOfOrigin.getCountryName())
                .manufacturer(null)
                .packer(null)
                .productItems(itemDetailsList)     //  items list details
                .build();
    }

    //  USER side - product details - with items minimal details
    @Transactional
    public UserProductDetailsDto getUserProductDetails(String superSku) {

        //  getting PRODUCT ITEMS by SUPER SKU
        List<ProductItem> items = itemsRepo.findAllBySuperSku(superSku);
        if (items == null || items.isEmpty()) {
            throw new EntityNotFoundException("No product item found for super SKU - " + superSku);
        }

        //  getting PRODUCT from ITEMs
        Product product = items.stream()
                .findFirst()
                .map(i -> i.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product item not present"));

        //  getting VARIATION DETAILS
        List<VariationOption> options = items.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product item not present"))
                .getConfigurations().stream()
                .map(ProductConfiguration::getVariationOption)
                .toList();

        Map<String, String> variations = options.stream()
                .filter(opt -> {
                    String variationName = opt.getVariation().getVariationName();
                    return !variationName.equalsIgnoreCase("Colors")
                            && !variationName.equalsIgnoreCase("Size");
                }).collect(Collectors.toMap(
                        opt -> opt.getVariation().getVariationName(),
                        VariationOption::getOptionValue
                ));


        //  getting ITEMs - minimal details
        List<UserItemListingDto> itemDetailsList = getMinimalItemsDetails(
                product,
                "USER"
        );
        if (itemDetailsList == null || itemDetailsList.isEmpty()) {
            throw new RuntimeException("Item minimal details not found");
        }


//        Set<String> superSkus = product.getItems().stream()
//                .map(ProductItem::getSuperSku)
//                .collect(Collectors.toSet());



        //  other details
        String manufacturer = "ABFRL, Aditya Birla Fashion and Retail,Khacharakanahalli Village,Survey No 32 & 33 Soukya Road (IOC Road)-560067,Hosakote Taluk, Bangalore,Karnataka,India";
        String packer = "ABFRL, Aditya Birla Fashion and Retail,Khacharakanahalli Village,Survey No 32 & 33 Soukya Road (IOC Road)-560067,Hosakote Taluk, Bangalore,Karnataka,India";
        CountryOfOrigin country = countryOfOriginRepo.findById(product.getCountryOfOriginId())
                .orElseThrow(() -> new EntityNotFoundException("Country of origin not found with ID: " + product.getCountryOfOriginId()));


        //  returning PRODUCT DETAILS - with ITEMs minimal details
        return UserProductDetailsDto.builder()
                .productName(product.getProductName())
                .description(product.getProductDescription())

                .itemWeight(product.getItemWeight())
                .genericName(product.getGenericName())
                .manufacturer(manufacturer)
                .packer(packer)
                .countryOfOrigin(country.getCountryName())

                .pod(product.getPodAvailable())
                .variations(variations)
                .items(itemDetailsList)
                .build();
    }



    public List<String> getProductImages(String superSku) {
        List<ProductImage> images = productImagesRepo.findBySuperSku(superSku);
        return images.stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }


    /// /   ********* Utility methods *********

    private String getSortValue(String sortRequest) {
        return switch (sortRequest) {
            case "latest" -> {
                yield "latestCreatedAt,desc";
            }
            case "name,asc" -> {
                yield "productName,asc";
            }
            case "name,desc" -> {
                yield "productName,desc";
            }
            case "price,asc" -> {
                yield "minPrice,asc";
            }
            case "price,desc" -> {
                yield "maxPrice,desc";
            }
            //  ## STOCK sorting
            default -> throw new IllegalArgumentException("Invalid sortRequest: " + sortRequest);
        };
    }

    //  get ITEMs - minimal details
    @Transactional
    private <T> List<T> getMinimalItemsDetails(Product product, String target) {
        List<ProductItem> items = product.getItems();


        //  getting unique 'super SKUs'
        Set<String> superSkus = items.stream()
                .map(ProductItem::getSuperSku)
                .collect(Collectors.toSet());

        //  ADMIN - ITEM details processing
        if (target.equals("ADMIN")) {
            List<AdminItemListingDto> itemDtoList = new ArrayList<>();

            //  building item Dto for every super SKU
            for (String superSku : superSkus) {

                //  Aggregating for collective 'status' & 'stock' values & color details
                StringBuilder color = new StringBuilder();
                StringBuilder hexCode = new StringBuilder();

                AtomicInteger statusFlag = new AtomicInteger(0);
                AtomicInteger stockSum = new AtomicInteger(0);

                items.stream()
                        .filter(item -> superSku.equals(item.getSuperSku()))
                        .forEach(item -> {
                            if (item.getIsActive()) {
                                statusFlag.incrementAndGet();
                            }
                            stockSum.addAndGet(item.getQtyInStock());

                            if (color.length() == 0 && hexCode.length() == 0) {
                                item.getConfigurations().stream()
                                        .filter(config -> config.getVariationOption()
                                                .getVariation()
                                                .getVariationName()
                                                .equalsIgnoreCase("colors")
                                        ).findFirst()
                                        .ifPresent(config -> {
                                            VariationOption option = config.getVariationOption();
                                            color.append(option.getOptionValue());
                                            hexCode.append(option.getColorCode().getColorCode());
                                        });
                            }
                        });

                //  total item count with common super SKU
                long itemCount = items.stream()
                        .filter(item -> superSku.equals(item.getSuperSku()))
                        .count();

                ProductActiveStatus activeStatus = statusFlag.get() == itemCount
                        ? ProductActiveStatus.ACTIVE
                        : statusFlag.get() == 0 ? ProductActiveStatus.INACTIVE
                        : ProductActiveStatus.PARTIAL;

                //  get image icon url
                List<ProductImage> imageUrls = productImagesRepo.findBySuperSku(superSku);
                ProductImage imageIconUrl = imageUrls.stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No images found for super SKU: " + superSku));

                //  stock status calculation
                StockStatus stockStatus = getStockStatus(
                        til,
                        stockSum.get() / itemCount
                );

                //  building Item dto
                AdminItemListingDto item = AdminItemListingDto.builder()
                        .superSku(superSku)
                        .stockStatus(stockStatus)
                        .activeStatus(activeStatus)
                        .color(color.toString())
                        .hexCode(hexCode.toString())
                        .iconImage(imageIconUrl.getImageUrl())
                        .build();

                itemDtoList.add(item);
            }
            return (List<T>) itemDtoList;

        } else if (target.equals("USER")) {                 //  USER - ITEM details processing

            List<UserItemListingDto> itemDetailsList = new ArrayList<>();
            for (String ssku : superSkus) {
                ProductItem productItem = product.getItems().stream()
                        .filter(item -> item.getSuperSku().equalsIgnoreCase(ssku))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Item not found with super SKU: " + ssku));

                //  getting COLOR name
                String colorName = productItem.getConfigurations().stream()
                        .filter(config -> {
                            return config.getVariationOption()
                                    .getVariation()
                                    .getVariationName()
                                    .equalsIgnoreCase("Colors");
                        }).findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Color option not found for item with super SKU: " + ssku))
                        .getVariationOption().getOptionValue();

                //  getting ICON IMAGE
                String iconImage = productItem.getImages().stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No image found for item with super SKU: " + ssku))
                        .getImageUrl();

                //  getting SIZEs
                List<String> sizes = product.getItems().stream()
                        .filter(item -> item.getSuperSku().equalsIgnoreCase(ssku))
                        .map(item -> {
                            return item.getConfigurations().stream()
                                    .filter(config -> {
                                        return config.getVariationOption()
                                                .getVariation()
                                                .getVariationName()
                                                .equalsIgnoreCase("size");
                                    }).findFirst()
                                    .orElseThrow(() -> new EntityNotFoundException("Size option not found in Item with super SKU: " + ssku))
                                    .getVariationOption()
                                    .getOptionValue();
                        }).toList();

                UserItemListingDto itemDetails = UserItemListingDto.builder()
                        .superSku(ssku)
                        .colorName(colorName)
                        .iconImage(iconImage)
                        .price(productItem.getPrice())
                        .sizes(sizes)
                        .build();
                itemDetailsList.add(itemDetails);
            }
            return (List<T>) itemDetailsList;
        } else {
            return  null;
        }
    }

    //  get Stock availability status
    public StockStatus getStockStatus(long til, long currentStock) {
        if (currentStock >= til) {
            return StockStatus.IN_STOCK;
        } else if (currentStock > 0 && currentStock < til) {
            return StockStatus.LOW_STOCK;
        } else if (currentStock <= 0) {
            return StockStatus.OUT_OF_STOCK;
        } else if (currentStock >= 1.5 * til) {
            return StockStatus.OVER_STOCKED;
        } else {
            throw new IllegalArgumentException("Invalid StockStatus");
        }
    }


    //  ********* get product item details by given super SKU *********

    public ItemDetailsDto getItemDetails(String superSku) {
        boolean exists = itemsRepo.existsBySuperSku(superSku);
        if (!exists) throw new IllegalArgumentException("super SKU doesn't exist");

        List<ProductItem> items = itemsRepo.findAllBySuperSku(superSku);

        AtomicReference<Float> startingPrice = new AtomicReference<>(Float.MAX_VALUE);

        List<ItemSizeDto> sizeDetails = items.stream()
                .map(item -> {
                    if (item.getPrice() < startingPrice.get()) {
                        startingPrice.set(item.getPrice());
                    }
                    return ItemSizeDto.builder()
                            .itemId(item.getId())
                            .size(itemsRepo.findSizeByItemId("Size", item.getId()))
                            .sku(item.getSKU())
                            .qtyInStock(item.getQtyInStock())
                            .isActive(item.getIsActive())
                            .createdAt(item.getCreatedAt())
                            .build();
                }).toList();

        List<String> imageUrls = productImagesRepo.findBySuperSku(superSku).stream()
                .map(image -> image.getImageUrl())
                .toList();

        return ItemDetailsDto.builder()
                .startingPrice(startingPrice.get() != Float.MAX_VALUE ? startingPrice.get() : null)
                .imageUrls(imageUrls)
                .sizeDetails(sizeDetails)

//                .superSku()
//                .stockStatus()
//                .activeStatus()
//                .color()
//                .hexCode()
                .build();
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

    public ProductItemDetailsDto getProductItemDetailsById(Long itemId) {
        ProductItem p = itemsRepo.findById(itemId)
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


}


/// /   ******* utility methods *******

//  iconImage provider - by productId || productItemId
//    private String getIconImage(Long productId, Long productItemId) {
//        try {
/// /            if (productId != null && productItemId == null) {
/// /                List<ProductImage> productImages = productImagesRepo.findByProductId(productId);
/// /                return productImages.isEmpty() ? null : productImages.get(0).getImageUrl();
/// /            } else {
//                List<ProductImage> productItemImages = productImagesRepo.findByProductItemId(productItemId);
//                return productItemImages.isEmpty() ? null : productItemImages.get(0).getImageUrl();
/// /            }
//        } catch (Exception e) {
//            logger.error("Error fetching image for product ID: {}", productId, e);
//            return null;
//        }
//    }


//  List<iconImage> provider - by productId || productItemid
//    private List<String> getImagesById(Long productId, Long productItemId) {
//        try {
//            List<String> imageUrl = new ArrayList<>();
//
/// /            if (productId != null && productItemId == null) {
/// /                List<ProductImage> productImages = productImagesRepo.findByProductId(productId);
/// /                for(ProductImage p : productImages) {
/// /                    if (p.getImageUrl().isEmpty() || p == null) {
/// /                        continue;
/// /                    }
/// /                    imageUrl.add(p.getImageUrl());
/// /                }
/// /            } else {
//                List<ProductImage> productItemImages = productImagesRepo.findByProductItemId(productItemId);
//                for(ProductImage p : productItemImages) {
//                    if (p.getImageUrl().isEmpty() || p == null) {
//                        continue;
//                    }
//                    imageUrl.add(p.getImageUrl());
//                }
////            }
//            return imageUrl;
//        } catch (Exception e) {
//            logger.error("Error fetching images");
//            return null;
//        }
//    }

//  converter - ProductItem -> AdminItemListingDto
//    private AdminItemListingDto convertProductItemToProductItemListingDto(ProductItem productItem) {
//        try {
//            String sizeVariationOption = this.getVariationOfProductItemByProductItemId(productItem.getId(), "Size");
//            ProductActiveStatus activeStatus = productItem.getIsActive() == true ? ProductActiveStatus.ACTIVE : ProductActiveStatus.INACTIVE;
//
//            return new AdminItemListingDto(
//                    productItem.getId(),
//                    productItem.getSKU(),
//                    productItem.getPrice(),
//                    sizeVariationOption,
//                    productItem.getQtyInStock(),
//                    activeStatus,
//                    this.getIconImage(null, productItem.getId())
//            );
//        } catch (Exception e) {
//            logger.error("Error converting ProductItem to AdminItemListingDto. ProductItem ID: {}", productItem.getId(), e);
//            return null;
//        }
//    }

//  Get variationOption by productItemId
//    private String getVariationOfProductItemByProductItemId(Long productItemId, String variationName) {
//        try {
//            List<ProductConfiguration> configList = productConfigurationRepo.findAllByProductItemId(productItemId);
//            List<OptionMinimalDto> sizeOptions = variationsRepo.findOptionsByVariationName(variationName);
//
//            Set<Long> optionIds = configList.stream()
//                    .map(config -> config.getVariationOption().getId())
//                    .collect(Collectors.toSet());
//
//            return sizeOptions.stream()
//                    .filter(option -> optionIds.contains(option.getId()))
//                    .map(option -> option.getOptionValue())     // .map(OptionMinimalDto::getOptionValue)
//                    .findFirst()
//                    .orElse(null);
//        } catch (Exception e) {
//            logger.error("Error getting Option for Variation '{}' for productItem ID: {}", variationName, productItemId, e);
//            return null;
//        }
//    }

//    private Map<String, List<?>> getFilterValues(List<FilterRequestDto> filterRequests) {
//        return filterRequests.stream()
//                .collect(Collectors.toMap(
//                        FilterRequestDto::getFilterType,
//                        filter -> List.of(filter.getValues().split(","))
//                ));
//    }

///
//  sort generator order
//    private Sort.Order generateSortOrder(String sortRequest) {
//
//        System.out.println("near switch - " + sortRequest);
//        String sort = "createdAt,desc";
//        switch (sortRequest) {
//            case "latest":
//                sort = "createdAt,desc";
//                break;
//            case "name,asc":
//                sort = "name,asc";
//                break;
//            case "name,desc":
//                sort = "name,desc";
//                break;
//            case "price,asc":
//                sort = "price,asc";
//                break;
//            case "price,desc":
//                sort = "price,desc";
//                break;
////            case "featured":
////                sort = "";
////                break;
////            case "reviews":
////                sort = "";
////                break;
////            case "bestSelling":
////                sort = "";
////                break;
//            default:
//                sort = "createdAt,desc";
//        }
//        if (sort == null || sort.isEmpty()) {
//            throw new RuntimeException("Invalid sort request");
//        }
//        String[] parts = sort.split(",");
//        String property = parts[0];
//        Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
//                ? Sort.Direction.DESC
//                : Sort.Direction.ASC;
//        return new Sort.Order(direction, property);
//    }
///


//  Product page - Dto converter
//    private Page<AdminProductListingDto> convertToDto(Page<Product> products) {
//        return products.map(product -> {
//            return AdminProductListingDto.builder()
//                    .productId(product.getId())
//                    .productName(product.getProductName())
//                    .subCategoryName(product.getCategory().getCategoryName())
//                    .basePrice(35F)
//                    .totalItems(03)
//                    .activeStatus(ProductActiveStatus.ACTIVE)
//                    .iconImage("abc")
//                    .build();
//        });
//    }

//  converter - Product -> AdminProductListingDto
//    private AdminProductListingDto convertProductToProductListing(Product product) {
//        try {
//            List<ProductItem> productItemsList = productItemsRepo.findAllByProductId(product.getId());
//            int totalItems = 0;
//            float startingPrice = Float.MAX_VALUE;
//            boolean hasActive = false;
//            boolean hasInactive = false;
//
//            if (productItemsList == null) productItemsList = Collections.emptyList();
//            for (ProductItem item : productItemsList) {
//                if (item.getPrice() < startingPrice) {
//                    startingPrice = item.getPrice();
//                }
//                if (Boolean.TRUE.equals(item.getIsActive())) hasActive = true;
//                else hasInactive = true;
//
//                totalItems++;
//            }
//            ProductActiveStatus activeStatus = hasActive && !hasInactive ? ProductActiveStatus.ACTIVE
//                    : !hasActive && hasInactive ? ProductActiveStatus.INACTIVE
//                    : ProductActiveStatus.PARTIALLY_ACTIVE;
//            if (startingPrice == Float.MAX_VALUE) startingPrice = 0f;
//
//            return new AdminProductListingDto(
//                    product.getId(),
//                    product.getProductName(),
//                    product.getCategory().getCategoryName(),
//                    startingPrice,
//                    totalItems,
//                    activeStatus,
//                    this.getIconImage(product.getId(), null)
//            );
//        } catch (Exception e) {
//            logger.error("Error converting product to productListingDto. Product ID: {}", product.getId(), e);
//            return null;
//        }
//    }


/// /   ******* specification stuff *******

//    public List<?> getAllProductListingWithSpec(RequestDto requestDto) {
//        Specification<Product> productSpecifications = productSpecService
//                          .getFilterSpecification(requestDto.getFilterRequestDtos());
//        return productsRepo.findAll(productSpecifications);
//    }

//    public Page<AdminProductListingDto> getAllProductListing(RequestDto requestDto,
//                                                        Long categoryId,
//                                                        Integer page,
//                                                        Integer size) {
///        Page<Product> products;
///        Pageable sortedPageable = PageRequest.of(
///                page, size,
///                Sort.by(Sort.Direction.DESC, "createdAt")
///        );
///        if (requestDto != null) {
///            Specification<Product> productSpecs = productSpecService.getFilterSpecification(requestDto.getFilterRequestDtos());
///            products = productsRepo.findAll(productSpecs, sortedPageable);
///        } else if (requestDto == null) products = productsRepo.findAll(sortedPageable);
///        else throw new IllegalArgumentException("Check the request dto");
//
//        List<AdminProductListingDto> productListingDtos = products.stream()
//                .map(this::convertProductToProductListing)
//                .collect(Collectors.toList());
//
//    / /        System.out.println(productListingDtos);
//
//        return new PageImpl<>(productListingDtos, sortedPageable, products.getTotalElements());
//    }


/// /   ******* partial search stuff *******

//    public List<ProductSuggestionDto> partialSearchProducts(String productName) {
//        List<Product> results = productsRepo.findByProductNameContainingIgnoreCase(productName);
//        List<ProductSuggestionDto> suggestions = results.stream()
//                .map(product -> new ProductSuggestionDto(product.getId(), product.getProductName()))
//                .toList();
//        return suggestions;
//    }







