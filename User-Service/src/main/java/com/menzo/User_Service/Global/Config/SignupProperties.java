package com.menzo.User_Service.Global.Config;

import jakarta.ws.rs.DefaultValue;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app.signup")
public record SignupProperties(
        @DefaultValue("") List<String> allowedRedirectHosts,
        @DefaultValue("/") String defaultRedirectUrl,
        @DefaultValue("24h") Duration redirectTtl
) {
}
