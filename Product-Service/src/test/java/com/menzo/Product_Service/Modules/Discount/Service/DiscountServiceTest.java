package com.menzo.Product_Service.Modules.Discount.Service;

import com.menzo.Product_Service.Modules.Discount.Dto.CreateDiscountDto;
import com.menzo.Product_Service.Modules.Discount.Dto.DiscountMappingDto;
import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
class DiscountServiceTest {

    @Autowired
    private DiscountService discountService;

    //  test for Add new discount
    @Test
    public void testAddNewDiscount() {
        CreateDiscountDto newDiscount = CreateDiscountDto.builder()
                .discountCode("NY24-PA-26746-20P")
                .discountName("New year sale")
                .discountDescription("New year sale for 2024")
                .level(DiscountLevel.CATEGORY)
                .type(DiscountType.PERCENT)
                .discountStatus(PromotionStatus.ACTIVE)
                .value(BigDecimal.valueOf(20))
                .capType(CapType.PERCENT)
                .capValue(BigDecimal.valueOf(30))
                .startAt(LocalDateTime.of(2025, Month.DECEMBER, 28, 12, 35))
                .endAt(LocalDateTime.of(2026, Month.JANUARY, 4, 12, 35))
                .build();
        UUID uuid = discountService.addNewDiscount(newDiscount);

        System.out.println("Saved Id: " + uuid);
    }

    //  test for Update discount (Partial update)
    @Test
    public void testUpdateDiscount() {}

    //  test for Delete discount (Soft delete)
    @Test
    public void testSoftDeleteDiscount() {}


    /// /   ********* Test for Discount mapping *********

    //  test for failure: Mismatching discount level
    @Test
    public void levelFailureTestDiscountMapping() {
        try {
            DiscountMappingDto mappingDto = DiscountMappingDto.builder()
                    .discountId(UUID.fromString("64f108e3-a040-4ffc-b343-08a91e9d1f09"))
                    .level(DiscountLevel.SUB_CATEGORY)
                    .selectionList(Arrays.asList(121L, 122L, 123L))
                    .build();
            UUID uuid = discountService.discountMapping(mappingDto);
            System.out.println("Mapped discount ID: " + uuid);
        } catch (Exception e) {
            System.out.println("Discount mapping failure message: " + e.getMessage());
        }
    }

    //  test for failure: Mismatching Category/Sub-category for the level of the discount (eg): sub-category mapping for category level discount
    @Test
    public void mismatchEntityFailureTestDiscountMapping() {
        try {
            DiscountMappingDto mappingDto = DiscountMappingDto.builder()
                    .discountId(UUID.fromString("64f108e3-a040-4ffc-b343-08a91e9d1f09"))
                    .level(DiscountLevel.CATEGORY)
                    .selectionList(Arrays.asList(121L, 122L, 123L))
                    .build();
            UUID uuid = discountService.discountMapping(mappingDto);
            System.out.println("Mapped discount ID: " + uuid);
        } catch (Exception e) {
            System.out.println("Discount mapping failure message: " + e.getMessage());
        }
    }

    //  test for successful mapping
    @Test
    public void testDiscountMapping() {
        try {
            DiscountMappingDto mappingDto = DiscountMappingDto.builder()
                    .discountId(UUID.fromString("64f108e3-a040-4ffc-b343-08a91e9d1f09"))
                    .level(DiscountLevel.CATEGORY)
                    .selectionList(Arrays.asList(2L))
                    .build();
            UUID uuid = discountService.discountMapping(mappingDto);
            System.out.println("Mapped discount ID: " + uuid);
        } catch (Exception e) {
            System.out.println("Discount mapping failure message: " + e.getMessage());
        }
    }

}