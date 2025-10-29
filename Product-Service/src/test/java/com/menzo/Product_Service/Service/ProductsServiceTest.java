package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Entity.*;
import com.menzo.Product_Service.Repository.ProductConfigurationRepo;
import com.menzo.Product_Service.Repository.ProductItemsRepo;
import com.menzo.Product_Service.Repository.ProductsRepo;
import com.menzo.Product_Service.Repository.VariationsOptionsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ProductsServiceTest {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductConfigurationRepo productConfigurationRepo;

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ProductItemsRepo itemsRepo;



    //  add new product
    @Test
    public void testSaveNewProduct() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "saveNewProduct",
                NewProductDto.class,
                ProductCategory.class
        );
        method.setAccessible(true);

        NewProductDto newProductDto = NewProductDto.builder()
                .productName("Some product name 1")
                .description("Some product description")
                .pod("available")
                .itemWeight(23.3F)
                .genericName("Some generic name")
                .countryOfOrigin("India")
                .subCategoryId(121L)
                .build();

        ProductCategory sub = categoriesRetrievalService
                .getSubCategoryById(newProductDto.getSubCategoryId());
        Object invokedResult = method.invoke(
                productsService,
                newProductDto,
                sub
        );
        System.out.println(invokedResult);
    }

    //  add new product item
    @Test
    @Transactional
    @Rollback(value = true)
    public void testSaveNewProductItem() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "saveNewProductItem",
                Map.class,
                List.class,
                ProductCategory.class,
                Product.class,
                Long.class,
                Float.class,
                Boolean.class
        );
        method.setAccessible(true);
        System.out.println("method is now accessible - test");

        Map<Long, Integer> sizeStockMap = new HashMap<>();
        sizeStockMap.put(7L, 98);
        sizeStockMap.put(8L, 53);
        sizeStockMap.put(18L, 45);
        List<VariationOption> variations = optionsRepo
                .findByIdIn(Arrays.asList(
                        16L,
                        21L,
                        24L
                ));
        System.out.println("Getting sub-category - test");
        ProductCategory subCategory = categoriesRetrievalService
                .getSubCategoryById(121L);
        Product product = productsRepo.findById(73L)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: 73"));

        System.out.println("Invoking the method - Test");
        Object invoked = method.invoke(
                productsService,
                sizeStockMap,
                variations,
                subCategory,
                product,
                5L,
                335.50F,
                true
        );
        System.out.println(invoked);
    }

    @Test
    public void anotherFindTest() {
        List<ProductItem> all = itemsRepo.findAll();
        all.stream().forEach(a -> System.out.println(
                "[" + a.getId() + " - " +
                        a.getProduct().getId() +
                        " - " + a.getSKU()
        ));
    }



//    ********* utility methods *********

    @Test
    public void testAddCountryOfOrigin() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "addCountryOfOrigin",
                String.class
        );
        method.setAccessible(true);
        Long countryId = (Long) method.invoke(
                productsService,
                "Sudan"
        );
        System.out.println("Country ID: " + countryId);
    }

    @Test
    public void testProcessVariationsWithVariationsMap() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "processVariations",
                Map.class,
                List.class
        );
        method.setAccessible(true);

        Map<String, String> variationsMap = new HashMap<>();
        variationsMap.put("Fit type", "3");
        variationsMap.put("Sleeve", "5");
        variationsMap.put("Collar", "20");

        Object invokedResult = method.invoke(
                productsService,
                variationsMap,
                null
        );

        System.out.println(invokedResult);
    }

    @Test
    public void testProcessVariationsWithProductConfigList() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "processVariations",
                Map.class,
                List.class
        );
        method.setAccessible(true);

        List<ProductConfiguration> configList = productConfigurationRepo.findAllByProductItemId(63L);
        Object invokedResult = method.invoke(
                productsService,
                null,
                configList
        );
        System.out.println(invokedResult);
    }

    @Test
    public void testGenerateSKU() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "generateSKU",
                String.class,
                String.class,
                Long.class,
                String.class,
                String.class,
                Long.class
        );
        method.setAccessible(true);
        String sku = (String) method.invoke(
                productsService,
                "J-23-SG",
                null,
                null,
                null,
                "L",
                44L
        );

        System.out.println(sku);
    }

}