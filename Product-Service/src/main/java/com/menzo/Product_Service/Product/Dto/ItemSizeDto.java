package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSizeDto {

    private Long itemId;

    private String size;

    private String sku;

    private Integer qtyInStock;

//    private Float price;

    private boolean isActive;

    private Date createdAt;

//    private Date updatedAt;

}
