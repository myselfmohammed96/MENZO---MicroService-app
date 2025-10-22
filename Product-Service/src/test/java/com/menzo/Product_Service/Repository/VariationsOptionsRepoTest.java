package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.VariationOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        List<VariationOption> optionsList = optionsRepo.findByIdIn(List.of(60L, 61L, 5L));
        System.out.println("Options list: " + optionsList);
    }

}