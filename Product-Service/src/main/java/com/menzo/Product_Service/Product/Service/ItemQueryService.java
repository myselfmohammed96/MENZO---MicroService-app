package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.Product.Dto.ItemDto.ItemDetailsDto;
import com.menzo.Product_Service.Product.Dto.ItemDto.ItemImageDto;
import com.menzo.Product_Service.Product.Dto.ItemSizeDto;
import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Repository.ProductImagesRepository;
import com.menzo.Product_Service.Product.Repository.ProductItemsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ItemQueryService {

    @Autowired
    private ProductItemsRepository itemsRepo;

    @Autowired
    private ProductImagesRepository productImagesRepo;



    //  ********* get product item details by given super SKU *********

    public ItemDetailsDto getItemDetails(String superSku) {
        boolean exists = itemsRepo.existsBySuperSku(superSku);
        if (!exists) throw new IllegalArgumentException("super SKU doesn't exist");

        List<ProductItem> items = itemsRepo.findAllBySuperSku(superSku);

//        AtomicReference<Float> startingPrice = new AtomicReference<>(Float.MAX_VALUE);
        AtomicReference<BigDecimal> baseMrp = new AtomicReference<>(BigDecimal.valueOf(Float.MAX_VALUE));
        AtomicReference<BigDecimal> baseSelling = new AtomicReference<>(BigDecimal.valueOf(Float.MAX_VALUE));

        List<ItemSizeDto> sizeDetails = items.stream()
                .map(item -> {
                    if (item.getMrp().compareTo(baseMrp.get()) < 0) {
                        baseMrp.set(item.getMrp());
                    }
                    if (item.getSellingPrice().compareTo(baseSelling.get()) < 0) {
                        baseSelling.set(item.getSellingPrice());
                    }
//                    if (item.getPrice() < startingPrice.get()) {
//                        startingPrice.set(item.getPrice());
//                    }
                    return ItemSizeDto.builder()
                            .itemId(item.getItemId())
                            .size(itemsRepo.findSizeByItemId("Size", item.getItemId()))
                            .sku(item.getSKU())
                            .qtyInStock(item.getQtyInStock())
                            .isActive(item.isActive())
                            .createdAt(item.getCreatedAt())
                            .build();
                }).toList();

        List<ItemImageDto> imageUrls = productImagesRepo.findBySuperSku(superSku).stream()
//                .map(image -> image.getImageUrl())
                .map(image -> ItemImageDto.builder()
                        .productImageId(image.getProductImageId())
                        .imageUrl(image.getImageUrl())
                        .imageOrder(image.getImageOrder())
                        .isPrimaryImage(image.isPrimaryImage())
                        .build()
                ).toList();

        return ItemDetailsDto.builder()
                .baseMrp(baseMrp.get() != BigDecimal.valueOf(Float.MAX_VALUE) ? baseMrp.get() : null)
                .baseSellingPrice(baseSelling.get() != BigDecimal.valueOf(Float.MAX_VALUE) ? baseSelling.get() : null)
//                .startingPrice(startingPrice.get() != Float.MAX_VALUE ? startingPrice.get() : null)
                .imageUrls(imageUrls)
                .sizeDetails(sizeDetails)

//                .superSku()
//                .stockStatus()
//                .activeStatus()
//                .color()
//                .hexCode()
                .build();
    }

    public String getSkuByItemId(UUID itemId) {
        ProductItem item = itemsRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Product-item not found with ID: " + itemId));
        return item.getSKU();
    }

}
