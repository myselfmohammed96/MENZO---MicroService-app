package com.menzo.Home_Service.UrlConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.route.store-front")
public record StoreFrontRouteUrls(
        Page page
) {
    public record Page(
            String index
    ) {}
}
