package com.menzo.Home_Service.UrlConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.api.user-auth")
public record UserAuthApiUrls(
        String checkEmailExistence
) {}
