package com.menzo.Product_Service.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ColorCodeRepoTest {

    @Autowired
    private ColorCodeRepo colorCodeRepo;

    @Test
    public void TestExistsByColorAbbreviation() {
        boolean exists = colorCodeRepo.existsByColorAbbreviation("RD");
        System.out.println("colorCode exists: " + exists);
    }
}
