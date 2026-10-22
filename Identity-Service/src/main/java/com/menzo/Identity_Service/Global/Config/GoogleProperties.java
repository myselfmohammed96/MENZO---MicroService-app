package com.menzo.Identity_Service.Global.Config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.google")
public record GoogleProperties(
        @NotBlank
        String clientId
) {
}
