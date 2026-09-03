package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSizeDto {

    private UUID itemId;

    private String size;

    private String sku;

    private Integer qtyInStock;

//    private Float price;

    private boolean isActive;

    private LocalDateTime createdAt;

//    private Date updatedAt;

}
