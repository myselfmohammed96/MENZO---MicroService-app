package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.Category.Dto.ParentCategoryView;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Product.Dto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Dto.ItemSizeDto;
import com.menzo.Product_Service.Product.Dto.CreateProductItemDto;
import com.menzo.Product_Service.Product.Dto.SizeDetailsDto;
import com.menzo.Product_Service.Product.Entity.Product;
import com.menzo.Product_Service.Product.Entity.ProductConfiguration;
import com.menzo.Product_Service.Product.Entity.ProductImage;
import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Product.Enum.StockStatus;
import com.menzo.Product_Service.Product.Repo.ProductItemsRepo;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemCommandService {

    private static final Logger logger = LoggerFactory.getLogger(ItemCommandService.class);

    @Autowired
    private OptionQueryService optionQueryService;

    @Autowired
    private ProductUtilityService productUtilityService;

    @Autowired
    private ProductItemsRepo itemsRepo;


    /*
     *
     *   Add new product-item
     *
     *   Every new PRODUCT ITEM saved  ->  will be associated with an existing PRODUCT
     *   Every new PRODUCT ITEM will have  ->  One 'color variation' unique to the PRODUCT ITEM & Multiple 'Size variations'
     *   Every new 'Add PRODUCT ITEM'  ->  will create multiple PRODUCT ITEM objects for given number of 'Size variations'
     *
     */
    @Transactional
    public ItemDetailsDto addNewProductItem(CreateProductItemDto newProductItem,
                                            List<SizeDetailsDto> sizeDetails,
                                            List<MultipartFile> images) throws IOException {

        //  --------- Data Pre-processing ---------
        //  getting PRODUCT, CATEGORY & SUB-CATEGORY by 'product ID'
        logger.info("Add new item: Data pre-processing");
        Product product = productsRepo.findById(newProductItem.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + newProductItem.getProductId()));

        ParentCategoryView category = categoriesRetrievalService.getParentByProductId(product.getId());
        ProductCategory subCategory = product.getSubCategory();

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
                    newProductItem.getStatus().equalsIgnoreCase("active")
            );
            savedItems.add(savedItem);

            if (savedItem.getMrp().compareTo(baseMrp) < 0) {
                baseMrp = savedItem.getMrp();
            }
            if (savedItem.getSellingPrice().compareTo(baseSellingPrice) < 0) {
                baseSellingPrice = savedItem.getSellingPrice();
            }
            if (savedItem.getIsActive()) {
                statusFlag++;
            }
            stockSum += savedItem.getQtyInStock();

            ItemSizeDto sizeDto = ItemSizeDto.builder()
                    .itemId(savedItem.getItemId())
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
                stockSum / itemCount
        );

        ProductActiveStatus activeStatus = statusFlag == itemCount
                ? ProductActiveStatus.ACTIVE
                : statusFlag == 0 ? ProductActiveStatus.INACTIVE
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


    //  Save PRODUCT ITEMs - *** DONE ***
    /*
     *
     *   Save new product item
     *
     */
    @Transactional
    ProductItem saveNewItem(Product product,
                            String superSku,
                            SizeDetailsDto sizeDetail,
                            List<VariationOption> variations,
                            VariationOption color) {

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
                .mrp(sizeDetail.getSizeMrp())
                .sellingPrice(sizeDetail.getSizeSellingPrice())
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

}
