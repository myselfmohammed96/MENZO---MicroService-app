package com.menzo.Home_Service.UrlConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(UserAuthRouteUrls.class)
class UserAuthRouteUrlsTest {

    @Autowired
    private UserAuthRouteUrls routeUrls;

    @Test
    public void runPageUrlTest() {
        System.out.println(routeUrls.page());
    }

    @Test
    public void runActionUrlTest() {
        System.out.println(routeUrls.action());
    }

}


//<!--<script th:inline="javascript">-->
//<!--    const API = {-->
//<!-- /*       redirectUrl: /*[[${signInPageUrls.redirectUrl}]]*/,*/-->
//<!--/*        checkEmailExistenceUrl: /*[[${signInPageUrls.checkEmailExistence}]]*/,*/-->
//<!--/*        otpVerificationPage: /*[[${signInPageUrls.otpVerificationPage}]]*/-->
//<!--    };-->
//<!--</script>-->