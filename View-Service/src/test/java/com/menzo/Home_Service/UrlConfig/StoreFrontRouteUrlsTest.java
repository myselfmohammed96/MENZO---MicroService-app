package com.menzo.Home_Service.UrlConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableConfigurationProperties(StoreFrontRouteUrls.class)
class StoreFrontRouteUrlsTest {

    @Autowired
    private StoreFrontRouteUrls routes;

    @Test
    void shouldBindRoutesFromYaml() {
        assertNotNull(routes.page());
        assertEquals("/index", routes.page().index());

        System.out.println(routes.page().index());
    }
}