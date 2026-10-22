package com.menzo.Identity_Service.Global.Config;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        @DefaultValue("5") @Min(1) int maxFailedLogins,
        @DefaultValue("15m") Duration failureWindow,
        @DefaultValue("15m") Duration lockDuration
) {
}
