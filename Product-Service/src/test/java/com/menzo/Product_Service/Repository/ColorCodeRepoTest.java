package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.ColorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ColorCodeRepoTest {

    @Autowired
    private ColorCodeRepo colorCodeRepo;

    @Test
    public void testFindAllColorCode() {
        List<ColorCode> allColorCodes = colorCodeRepo.findAll();
        System.out.println("Color Codes List: " + allColorCodes);
    }

    @Test
    public void TestExistsByColorAbbreviation() {
        boolean exists = colorCodeRepo.existsByColorAbbreviation("RD");
        System.out.println("colorCode exists: " + exists);
    }
}
