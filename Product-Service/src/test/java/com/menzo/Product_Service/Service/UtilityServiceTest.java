package com.menzo.Product_Service.Service;

import org.aspectj.weaver.bcel.Utility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

@SpringBootTest
class UtilityServiceTest {

    @Autowired
    private UtilityService utilityService;

    @Test
    public void testIsAbbreviationExists() throws Exception {
        Method method = UtilityService.class.getDeclaredMethod("isAbbreviationExists", String.class, StringBuilder.class);
        method.setAccessible(true);
        String abbreviation = (String) method.invoke(utilityService,
                "sub-category",
                new StringBuilder("S")
        );
        System.out.println(abbreviation);
    }
}