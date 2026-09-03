package com.menzo.Product_Service.Product.Dto.ItemDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateItemDto {

    @NotNull
    @Positive
    private UUID productId;

    @NotNull
    @Positive
    private  UUID colorId;

    private String activeStatus;

//    private List<SizeDetailsDto> sizeDetails;

}
