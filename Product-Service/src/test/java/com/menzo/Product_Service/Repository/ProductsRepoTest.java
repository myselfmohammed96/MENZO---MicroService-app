package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Modules.Product.Dto.ProductListingView;
import com.menzo.Product_Service.Modules.Product.Entity.Product;
import com.menzo.Product_Service.Modules.Product.Repo.ProductsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductsRepoTest {

    @Autowired
    private ProductsRepo productsRepo;

    @Test
    public void testFindAllWithItems() {
        List<Product> allItems = productsRepo.findAllWithItems();
        System.out.println(allItems);
    }

    @Test
    public void testFindAllProductsWithSorting() {
        List<ProductListingView> allProducts = productsRepo.findAdminProductsListing();
        allProducts.stream().forEach(product -> {
            System.out.println("Product: id-" + product.getProductId() +
                    ", \nname-" + product.getProductName() +
                    ", \nsubCategoryName-" + product.getSubCategoryName() +
                    ", \ncategoryName-" + product.getCategoryName() +
                    ", \nminPrice-" + product.getMinPrice() +
                    ", \nmaxPrice-" + product.getMaxPrice() +
                    ", \nminStockQty-" + product.getMinStockQty() +
                    ", \nmaxStockQty-" + product.getMaxStockQty() +
                    ", \nlatestCreatedAt-" + product.getLatestCreatedAt() +
                    ", \noldestCreatedAt-" + product.getOldestCreatedAt() +
                    ", \nactiveStatus-" + product.getActiveStatus() + "\n");
        });
        System.out.println(allProducts.size());
    }

    @Test
    public void testFindByCategory() {
        List<Product> byCategory = productsRepo.findByCategoryId(121L);
        for (Product p : byCategory) {
            System.out.println(p);
        }
    }

}