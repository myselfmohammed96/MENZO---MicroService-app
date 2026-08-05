package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.Product.Entity.ProductConfiguration;
import com.menzo.Product_Service.Product.Entity.ProductImage;
import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Repo.ProductImagesRepository;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class ProductUtilityService {

    private static final Logger logger = LoggerFactory.getLogger(ProductUtilityService.class);

    @Autowired
    private OptionQueryService optionQueryService;

    @Autowired
    private ProductImagesRepository imagesRepo;


    /*
     *
     *   Variation processing
     *   Provides the variation details of the product
     *   Other than 'size' & 'color' variations
     *   ## no validation for if the optionIds in value is bound with the key data or not
     *
     */
    List<VariationOption> processVariations(Map<String, String> variationDetailsMap,
                                            List<ProductConfiguration> productConfigs) {
        if (variationDetailsMap != null && productConfigs == null) {
            //  process for product saving
            logger.info("Processing variations: variations map");
            List<Long> optionIds = variationDetailsMap.values().stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            return optionQueryService.getOptionsByIds(optionIds);

        } else if (variationDetailsMap == null && productConfigs != null) {
            //  process for product-item saving
            logger.info("Processing variations: config list");

            //  fetching the IDs of all 'size' & 'color' options available in DB
            List<Long> optionIds = new ArrayList<>(optionQueryService.getOptionIdsByVariation("Size"));
            optionIds.addAll(optionQueryService.getOptionIdsByVariation("Colors"));

            //  getting the variation options of the product available in the ProductConfiguration table - other than 'size' & 'color'
            return productConfigs.stream()
                    .map(ProductConfiguration::getVariationOption)
                    .filter(option -> !optionIds.contains(option.getOptionId()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }


    /*
     *
     *   Generate SKU
     *
     */
    String generateSKU(String superSku,
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


    /*
     *
     *   Save images
     *
     */
    List<ProductImage> saveImages(String categoryName,
                                  String subCategoryName,
                                  Long productId,
                                  String superSku,
                                  List<ProductItem> productItems,
                                  Map<String, MultipartFile> images) throws IOException {

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

        //  creating - Directory with custom path
        Path uploadDir = Paths.get(
                "uploads",
                categoryName,
                subCategoryName,
                productId.toString(),
                superSku
        );
        Files.createDirectories(uploadDir);

        //  image processing
        logger.info("Saving images for super SKU: {}", superSku);
        List<ProductImage> imageEntities = images.entrySet().stream()
                .map(imageEntry -> {
                    //  ------- image order number -------
                    String imageOrderString = imageEntry.getKey();      // assuming keyString looks like -> images[0]
                    int imageOrder = (int) imageOrderString.charAt(imageOrderString.length() - 2);

                    boolean isPrimaryImage = imageOrder == 0;

                    //  ------- image file -------
                    MultipartFile image = imageEntry.getValue();

                    //  image file name processing
                    String contentType = image.getContentType();
                    String name = image.getOriginalFilename().toLowerCase();

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
                    String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                    String filename = UUID.randomUUID() + "." + extension;

                    //  image file path & storing
                    Path filePath = Paths.get(String.valueOf(uploadDir), filename);
                    try {
                        image.transferTo(filePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return ProductImage.builder()
                            .imageUrl(String.valueOf(filePath))
                            .superSku(superSku)
                            .imageOrder(imageOrder)
                            .isPrimaryImage(isPrimaryImage)
                            .productItems(productItems)
                            .build();
                }).toList();

        return imagesRepo.saveAll(imageEntities);
    }


    /*
     *
     *   Update product-item images
     *
     */
//    boolean updateImages(Product product) {}

}
