package com.menzo.Identity_Service.Global.Config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.internal")
public record InternalProperties(
        @NotBlank
        @Size(min = 32, message = "app.internal.api-key must be at least 32 characters")
        String apiKey
) {
}
