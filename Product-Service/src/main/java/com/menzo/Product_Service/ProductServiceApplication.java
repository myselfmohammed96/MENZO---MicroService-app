package com.menzo.Product_Service;

import com.menzo.Product_Service.Entity.ColorCode;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Repository.CategoriesRepo;
import com.menzo.Product_Service.Repository.VariationsOptionsRepo;
import com.menzo.Product_Service.Service.UtilityService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
