package com.menzo.Product_Service.Modules.Variation.Repo;

import com.menzo.Product_Service.Modules.Variation.Entity.VariationOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class VariationsOptionsRepoTest {

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    @Test
    public void testExistsByOptionValueAndVariationId() {
        boolean exists = optionsRepo.existsByOptionValueAndVariationId(
                "L",
                Long.valueOf(9)
        );
        System.out.println("Option exists: " + exists);
    }

    @Test
    @Transactional
    public void testFindByIdIn() {
        List<VariationOption> optionsList = optionsRepo.findByIdIn(List.of(
                60L,
                61L,
                5L
        ));
        System.out.println("Options list: " + optionsList);
        for (VariationOption o : optionsList) {
            System.out.println(o.getColorCode());
        }
    }

    @Test
    public void testFindByVariationId() {
        List<VariationOption> options = optionsRepo.findByVariationId(3L);
        System.out.println(options);
        System.out.println(options.size());
    }

}