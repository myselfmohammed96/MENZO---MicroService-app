package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.Category.Dto.ParentCategoryView;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Service.CategoryQueryService;
import com.menzo.Product_Service.GlobalComponents.Enum.ProductComponents;
import com.menzo.Product_Service.Product.Dto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Dto.ItemDto.ItemImageDto;
import com.menzo.Product_Service.Product.Dto.ItemDto.PriceDto;
import com.menzo.Product_Service.Product.Dto.ItemSizeDto;
import com.menzo.Product_Service.Product.Dto.CreateProductItemDto;
import com.menzo.Product_Service.Product.Dto.SizeDetailsDto;
import com.menzo.Product_Service.Product.Entity.Product;
import com.menzo.Product_Service.Product.Entity.ProductConfiguration;
import com.menzo.Product_Service.Product.Entity.ProductImage;
import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Product.Enum.StockStatus;
import com.menzo.Product_Service.Product.Repo.ProductItemsRepository;
import com.menzo.Product_Service.Product.Repo.ProductsRepository;
import com.menzo.Product_Service.Variation.Entity.ColorCode;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Service.ColorQueryService;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemCommandService {

    private static final Logger logger = LoggerFactory.getLogger(ItemCommandService.class);

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private ProductUtilityService productUtilityService;

    @Autowired
    private ImageQueryService imageQueryService;

    @Autowired
    private VariationQueryService variationQueryService;

    @Autowired
    private OptionQueryService optionQueryService;

    @Autowired
    private ProductItemsRepository itemsRepo;

    @Autowired
    private ColorQueryService colorQueryService;

    //  TARGET_INVENTORY_LEVEL
    private static Long til;

    @Value("#{'${target-inventory-level}'}")
    public void setTil(String til) {
        this.til = Long.valueOf(til);
    }


    /*
     *
     *   Add new product-item
     *
     *   Every new product-item saved  ->  will be associated with an existing PRODUCT
     *   Every new product-item will have  ->  One 'color variation' unique to the product-item & Multiple 'Size variations'
     *   Every new 'Add product-item'  ->  will create multiple product-item objects for given number of 'Size variations'
     *
     */
    @Transactional
    public ItemDetailsDto addNewProductItem(CreateProductItemDto newProductItem,
                                            List<SizeDetailsDto> sizeDetails,
                                            Map<String, MultipartFile> images) throws IOException {

        //  --------- Data Pre-processing ---------
        //  getting product, parent category, sub-category
        logger.info("Add new item: Data pre-processing");
        Product product = productsRepo.findById(newProductItem.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + newProductItem.getProductId()));

        ParentCategoryView parentCategory = categoryQueryService.getParentByProductId(product.getProductId());
        ProductCategory subCategory = product.getSubCategory();

        //  getting list of PRODUCT_VARIATION_CONFIGURATION from 1st found 'productItem' of 'product'
        List<ProductConfiguration> configs = product.getItems()
                .get(0)
                .getConfigurations();
        if (configs == null || configs.isEmpty()) {
            throw new EntityNotFoundException("Product doesn't have any Items");
        }

        //  getting VARIATION OPTIONS - excluding 'color' & 'size'
        List<VariationOption> variationOptionList = productUtilityService.processVariations(
                null,
                configs
        );
        if (variationOptionList == null || variationOptionList.isEmpty()) {
            throw new RuntimeException("Variations unavailable. Error while processing variationsMap.");
        }

        //  getting COLOR variation option by 'color ID'
        VariationOption color = optionQueryService.getOptionByIdAndVariationName(
                newProductItem.getColorId(),
                "Colors"
        );

        //  generating SUPER SKU
        String superSku = productUtilityService.generateSKU(
                null,
                subCategory.getAbbreviation(),
                product.getProductId(),
                color.getColorCode().getColorAbbreviation(),
                null
        );

        //  --------- saving PRODUCT ITEMS ---------
        BigDecimal baseMrp = BigDecimal.valueOf(Float.MAX_VALUE);
        BigDecimal baseSellingPrice = BigDecimal.valueOf(Float.MAX_VALUE);
        int statusFlag = 0;
        int stockSum = 0;

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
                    newProductItem.getActiveStatus().equalsIgnoreCase("active")
            );
            savedItems.add(savedItem);

            if (savedItem.getMrp().compareTo(baseMrp) < 0) {
                baseMrp = savedItem.getMrp();
            }
            if (savedItem.getSellingPrice().compareTo(baseSellingPrice) < 0) {
                baseSellingPrice = savedItem.getSellingPrice();
            }
            if (savedItem.isActive()) {
                statusFlag++;
            }
            stockSum += savedItem.getQtyInStock();

            ItemSizeDto sizeDto = ItemSizeDto.builder()
                    .itemId(savedItem.getItemId())
                    .size(sizeDetail.getSizeValue())
                    .sku(savedItem.getSKU())
                    .qtyInStock(savedItem.getQtyInStock())
                    .isActive(savedItem.isActive())
                    .createdAt(savedItem.getCreatedAt())
                    .build();

            sizeDetailDtos.add(sizeDto);
        }

        //  --------- returning ITEM DETAILS ---------
        //  get stockStatus & activeStatus
        logger.info("Deriving stock & active status");
        long itemCount = savedItems.size();
        StockStatus stockStatus = productQueryService.getStockStatus(
                til,
                stockSum / itemCount
        );

        ProductActiveStatus activeStatus = statusFlag == itemCount
                ? ProductActiveStatus.ACTIVE
                : statusFlag == 0 ? ProductActiveStatus.INACTIVE
                  : ProductActiveStatus.PARTIAL;

        List<ProductImage> savedImages = productUtilityService.saveImages(
                parentCategory.getCategoryName(),
                subCategory.getCategoryName(),
                product.getProductId(),
                superSku,
                savedItems,
                images
        );

        List<ItemImageDto> imageUrls = savedImages.stream()
                .map(image -> ItemImageDto.builder()
                        .productImageId(image.getProductImageId())
                        .imageUrl(image.getImageUrl())
                        .imageOrder(image.getImageOrder())
                        .isPrimaryImage(image.isPrimaryImage())
                        .build())
                .toList();

        logger.info("Returning item details for super SKU: {}", superSku);
        return ItemDetailsDto.builder()
                .baseMrp(baseMrp)
                .baseSellingPrice(baseSellingPrice)
                .imageUrls(imageUrls)
                .sizeDetails(sizeDetailDtos)
                .superSku(superSku)
                .stockStatus(stockStatus)
                .activeStatus(activeStatus)
                .color(color.getOptionValue())
                .hexCode(color.getColorCode().getColorHexCode())
                .build();
    }


    /*
     *
     *   Save new product-item
     *
     */
    @Transactional
    ProductItem saveNewItem(Product product,
                            String superSku,
                            SizeDetailsDto sizeDetail,
                            List<VariationOption> variations,
                            VariationOption color,
                            boolean isActive) {

        //  --------- Data Pre-processing ---------
        //  product object validation
        if (product == null || product.getProductId() == null || product.getProductId() <= 0) {
            throw new IllegalArgumentException("Product or productId required.");
        }

        //  getting the 'size' by ID
        VariationOption size = optionQueryService.getOptionByIdAndVariationName(
                sizeDetail.getSizeId(),
                "Size"
        );

        // generating sku - concatenating super sku with size & next sequenced item ID
        String sku = productUtilityService.generateSKU(
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
                .qtyInStock(sizeDetail.getSizeStock())
                .sellingPrice(sizeDetail.getSizeSellingPrice())
                .mrp(sizeDetail.getSizeMrp())
                .isActive(isActive)
                .build();

        ProductItem saved = itemsRepo.save(item);

        //  creating a list of PRODUCT & VARIATION CONFIGURATION for each PRODUCT ITEM
        List<ProductConfiguration> configs = variations.stream()
                .map(opt -> ProductConfiguration.builder()
                        .productItem(saved)
                        .variationOption(opt)
                        .build()
                ).collect(Collectors.toList());

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

        return itemsRepo.save(saved);
    }


    /*
     *
     *   Update product-item color
     *   Product-item identified by item ID
     *   Color identified by color code object ID (colorId)
     *
     */
    @Transactional
    public boolean updateItemColor(Long itemId,
                                   Long colorId) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));
        List<ProductConfiguration> itemConfigs = item.getConfigurations();

        //  fetching color option config of the product-item
        ProductConfiguration colorOptionConfig = itemConfigs.stream()
                .filter(config -> {
                    Variation variation = config.getVariationOption().getVariation();
                    return variation != null
                            && variation.getVariationName() != null
                            && variation.getVariationName()
                            .equalsIgnoreCase(ProductComponents.COLOR.name());
                }).findFirst()
                .orElseThrow(() -> new NotFoundException("Color variation not found in product-item with ID: " + itemId));

        if (Objects.equals(
                colorOptionConfig.getVariationOption().getColorCode().getColorCodeId(),
                colorId)) {
            return true;
        }
        ColorCode latestColor = colorQueryService.getColorCodeEntityById(colorId);
        VariationOption latestColorOption = latestColor.getColorOption();
        colorOptionConfig.setVariationOption(latestColorOption);

        return true;
    }


    /*
     *
     *   Update product-item size
     *   Product-item identified by item ID
     *   Size identified by size option ID (sizeId)
     *
     */
    @Transactional
    public boolean updateItemSize(Long itemId,
                                  Long sizeId) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));
        List<ProductConfiguration> itemConfigs = item.getConfigurations();

        //  fetching size option config of the product-item
        ProductConfiguration sizeOptionConfig = itemConfigs.stream()
                .filter(config -> {
                    Variation variation = config.getVariationOption().getVariation();
                    return variation != null
                            && variation.getVariationName() != null
                            && variation.getVariationName()
                            .equalsIgnoreCase(ProductComponents.SIZE.name());
                }).findFirst()
                .orElseThrow(() -> new NotFoundException("Size variation not found in product-item with ID: " + itemId));

        if (Objects.equals(
                sizeOptionConfig.getVariationOption().getOptionId(),
                sizeId)) {
            return true;
        }
        VariationOption latestSizeOption = optionQueryService.getOptionByIdAndVariationName(sizeId, ProductComponents.SIZE.name());
        sizeOptionConfig.setVariationOption(latestSizeOption);

        return true;
    }


    /*
     *
     *   Update product-item stock quantity
     *   Product-item identified by item ID
     *
     */
    public boolean updateItemStockQuantity(Long itemId,
                                           Integer latestStockQty) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));

        item.setQtyInStock(latestStockQty != null
                ? latestStockQty
                : item.getQtyInStock()
        );
        itemsRepo.save(item);
        return true;
    }


    /*
     *
     *   Update product-item prices
     *   Both selling price & MRP
     *   Product-item identified by item ID
     *
     */
    public boolean updateItemPrices(Long itemId,
                                    PriceDto latestPrices) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));

        item.setSellingPrice(latestPrices.getSellingPrice() != null
                && latestPrices.getSellingPrice().compareTo(BigDecimal.ZERO) > 0
                ? latestPrices.getSellingPrice()
                : item.getSellingPrice());

        item.setMrp(latestPrices.getMrp() != null
                && latestPrices.getMrp().compareTo(BigDecimal.ZERO) > 0
                ? latestPrices.getMrp()
                : item.getMrp());

        itemsRepo.save(item);
        return true;
    }


    /*
     *
     *   Update product-item active status
     *   Product-item identified by item ID
     *
     */
    public boolean updateItemActiveStatus(Long itemId,
                                          boolean isActive) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));

        //  updating item active status
        item.setActive(isActive);
        return itemsRepo.save(item).isActive();
    }


    /*
     *
     *   Update product-item images
     *   Images belong to every color (super SKU)
     *
     */
    @Transactional
    public boolean updateItemImages(Long itemId,
                                    Map<String, MultipartFile> latestImages,
                                    Map<String, Integer> imageIds) {
        if (latestImages.size() != imageIds.size()) {
            throw new IllegalArgumentException("Images and image IDs are not equal in size.");
        }

        //  fetching images of product-item by product-item ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));
        List<ProductImage> images = item.getImages();
        Set<Long> imageFileIds = images.stream()
                .map(ProductImage::getProductImageId)
                .collect(Collectors.toSet());

        //  Getting upload directory path
        ProductImage firstImage = images.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product image list is empty for product-item ID: " + itemId));
        Path uploadDir = Paths.get(firstImage.getImageUrl()).getParent();

        //  Getting list of product-items
        List<ProductItem> productItems = firstImage.getProductItems();

        //  Getting super SKU of product-item
        String superSku = item.getSuperSku();

        //  finding intact files and new files
        Map<Long, Integer> intactFileIdsAndOrder = new HashMap<>();
        List<String> toAddFileOrderKeys = new ArrayList<>();

        latestImages.entrySet().stream()
                .forEach(e -> {
                    Integer imageId = imageIds.get(e.getKey());
                    Integer imageOrder = Integer.parseInt(e.getKey().substring(
                            e.getKey().indexOf("[") + 1,
                            e.getKey().indexOf("]")
                    ));

                    if (imageFileIds.contains(imageId.longValue())) {
                        intactFileIdsAndOrder.put(
                                imageId.longValue(),
                                imageOrder
                        );
                    }
                    if (imageId == null || imageId < 0) {      //  ### front-end must send -1 as id for new images
                        toAddFileOrderKeys.add(e.getKey());
                    }
                });

        //  updating the order of intact images
        images.stream()
                .filter(i -> intactFileIdsAndOrder.containsKey(i.getProductImageId()))
                .forEach(i -> i.setImageOrder(intactFileIdsAndOrder.get(i.getProductImageId())));

        //  removing 'to delete' image files
        Set<Long> toDeleteFileIds = new HashSet<>(imageFileIds);
        toDeleteFileIds.removeAll(intactFileIdsAndOrder.keySet());

        Set<ProductImage> toDeleteImages = images.stream()
                .filter(i -> toDeleteFileIds.contains(i.getProductImageId()))
                .collect(Collectors.toSet());

        toDeleteImages.forEach(i -> {
            Path filePath = Paths.get(i.getImageUrl());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        images.removeAll(toDeleteImages);

        //  adding 'to add' image files
        List<ProductImage> addedImages = toAddFileOrderKeys.stream()
                        .map(k -> {
                            MultipartFile image = latestImages.get(k);
                            int imageOrder = Integer.parseInt(k.substring(
                                    k.indexOf("[") + 1,
                                    k.indexOf("]")
                            ));

                            //  image file name processing
                            String contentType = image.getContentType();
                            String originalFilename = image.getOriginalFilename();

                            if (originalFilename == null || originalFilename.isBlank()) {
                                throw new IllegalArgumentException("Invalid image name");
                            }
                            String name = originalFilename.toLowerCase();

                            boolean validType = "image/png".equals(contentType)
                                    || "image/jpeg".equals(contentType)
                                    || contentType == null;
                            boolean validExt = name.endsWith(".png")
                                    || name.endsWith(".jpg")
                                    || name.endsWith(".jpeg");

                            //  image file - sanitizing with white listed formats
                            if (!(validType && validExt)) {
                                throw new IllegalArgumentException("Only PNG and JPG images are allowed.");
                            }
                            if (image.isEmpty()) {
                                throw new IllegalArgumentException("Invalid image input");
                            }

                            //  creating - image filename
                            String extension = FilenameUtils.getExtension(originalFilename);
                            String filename = UUID.randomUUID() + "." + extension;

                            //  image file path & storing
                            Path filePath = Paths.get(String.valueOf(uploadDir), filename);
                            try {
                                image.transferTo(filePath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            return ProductImage.builder()
                                    .imageFilename(filename)
                                    .imageUrl(String.valueOf(filePath))
                                    .superSku(superSku)
                                    .imageOrder(imageOrder)
                                    .productItems(productItems)
                                    .build();
                        }).toList();
        images.addAll(addedImages);

        images.stream().forEach(i -> {
            if (i.getImageOrder() == 0) {
                i.setPrimaryImage(true);
            } else {
                i.setPrimaryImage(false);
            }
        });

        return true;
    }



































    private ProductImage addImage(Map.Entry<String, MultipartFile> imageEntry) {
        return null;
    }


    /*
    *
    *   update image order (sorting)
    *
     */


    /*
     *
     *   Delete product-item (soft delete)
     *   Product-item identified by item ID
     *
     */
    public boolean deleteItem(Long itemId) {
        //  fetching product-item by ID
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));

        //  soft deleting product-item: set isDeleted to true if not already
        item.setDeleted(true);
        item.setDeletedAt(LocalDateTime.now());
        itemsRepo.save(item);
        logger.info("Deleted product-item with ID: {}", itemId);
        return true;
    }


    /*
     *
     *   ## variation updates
     *
     */
//    public boolean updateVariationsEtc() {}

}
