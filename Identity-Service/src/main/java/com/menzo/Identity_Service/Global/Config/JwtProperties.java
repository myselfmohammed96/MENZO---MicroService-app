package com.menzo.Identity_Service.Global.Config;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        @NotBlank String secret,
        @DefaultValue("shop-identity") String issuer,
        @DefaultValue("10d") Duration ttl,
        @DefaultValue("access_token") String cookieName,
        @DefaultValue("true") boolean cookieSecure,
        @DefaultValue("Lax") String cookieSameSite,
        @DefaultValue("/") String cookiePath,
        String cookieDomain
) {
}
