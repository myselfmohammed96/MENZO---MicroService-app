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

//    @Autowired
//    private VariationsOptionsRepo categoriesRepo;
//
//    @Autowired
//    private UtilityService utilityService;
//
//    @PostConstruct
//    public void fillSubCatAbb() {
//        List<VariationOption> all = categoriesRepo.findByVariationId(3L);
////        System.out.println(all);
//        Map<String, String> colors = new HashMap<>();
//        String darkGreen = colors.put("Dark Green", "#013220");
//        colors.put("Navy blue", "#000080");
//        colors.put("Black", "#000000");
//        colors.put("White", "#FFFFFF");
//        colors.put("Red", "#FF0000");
//        colors.put("Snow white", "#F8F8FF");
//        colors.put("Grey", "#808080");
//        colors.put("Light beige", "#faf0e6");
//        colors.put("Dark maroon", "#3C0008");
//        colors.put("Light pink", "#FFB6C1");
//        colors.put("Yellow green", "#9ACD32");
//        colors.put("Dark purple", "#301934");
//        colors.put("Dark Orange", "#FF8C00");
//        colors.put("Space grey", "#343d46");
//        colors.put("Peach", "#FFE5B4");
//        List<Long> ids = all.stream()
//                .map(a -> {
//                    for (Map.Entry<String, String> e : colors.entrySet()) {
//                        if (e.getKey().equals(a.getOptionValue())) {
//                            String abb = utilityService.generateAbbreviation("Colors", e.getKey());
//                            ColorCode code = ColorCode.builder()
//                                    .colorAbbreviation(abb)
//                                    .colorCode(e.getValue())
//                                    .colorOption(a)
//                                    .build();
//                            a.setColorCode(code);
//                            VariationOption saved = categoriesRepo.save(a);
//                            return saved.getId();
////                            a.setColorCode(new ColorCode());
//                        }
//                    }
//                    return null;
//                }).toList();
//
//        System.out.println(ids);

        //        List<ProductCategory> collected = all.stream()
//                .filter(a -> a.getParentCategoryId() != null && a.getAbbreviation() == null)
//                .map(a -> {
//                    String abb = utilityService.generateAbbreviation(
//                            "sub-category",
//                            a.getCategoryName()
//                    );
//                    a.setAbbreviation(abb);
//                    ProductCategory saved = categoriesRepo.save(a);
//                    return saved;
//                }).collect(Collectors.toList());
//        collected.stream()
//                .forEach(c -> System.out.println(c.getCategoryName() + " : " + c.getAbbreviation()));
//
//        long count = all.stream().filter(a -> a.getParentCategoryId() != null && a.getAbbreviation() == null)
//                .count();
//        System.out.println(count);



//    }

}
