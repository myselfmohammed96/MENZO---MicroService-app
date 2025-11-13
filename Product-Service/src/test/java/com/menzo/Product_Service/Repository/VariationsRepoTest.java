package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto;
import com.menzo.Product_Service.Entity.Variation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class VariationsRepoTest {

    @Autowired
    private VariationsRepo variationsRepo;



//    ********* existence check *********

    @Test
    public void testExistsById() {
        boolean exists = variationsRepo.existsById(9L);
        System.out.println(
                "id: " + 9L +
                "\nExists: " + exists
        );
    }

    @Test
    public void testExistsByVariationName() {
        boolean exists = variationsRepo.existsByVariationName("Sleeve");
        System.out.println(
                "name: Sleeve" +
                "\nExists: " + exists
        );
    }



    //  ********* find methods *********

    @Test
    public void testFindById() {
        Optional<Variation> variation = variationsRepo.findById(25L);
        System.out.println("Variation: " + variation.get());
    }

    @Test
    public void testFindByVariationName() {
        Optional<Variation> variation = variationsRepo.findByVariationName("Sleeve");
        System.out.println("Variation: " + variation.get());

    }

//    @Test
//    public void testFindAllBySubCategoryId() {
//        List<Object[]> list = variationsRepo.findAllBySubCategoryId(139L);
//        for(Object[] o : list) {
//            System.out.println(Arrays.toString(o));
//        }
//        System.out.println("subCategory -> " + list.size());
//    }

    @Test
    public void testFindAllByCategoryId() {
        List<Object[]> list = variationsRepo.findAllByCategoryId(139L, false);
        for (Object[] o : list) {
            System.out.println(Arrays.toString(o));
        }
        System.out.println("generic one -> " + list.size());
    }

    @Test
    public void testFindOptionsByVariationName() {
        List<OptionWithIdDto> options = variationsRepo.findOptionsByVariationName("Sleeve");
        System.out.println(options);
    }
}