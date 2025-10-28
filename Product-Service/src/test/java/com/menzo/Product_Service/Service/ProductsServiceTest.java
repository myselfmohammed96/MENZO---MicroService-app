package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Entity.ProductConfiguration;
import com.menzo.Product_Service.Repository.ProductConfigurationRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
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
                Long.class,
                String.class,
                String.class,
                Long.class
        );
        method.setAccessible(true);
        String sku = (String) method.invoke(
                productsService,
                "J1",
                23L,
                "SG",
                "L",
                44L
        );
        System.out.println(sku);
    }

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

}