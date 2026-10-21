package com.menzo.Communication_Service.Global.Config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app.otp")
public record OtpProperties(
        @NotBlank @Size(min = 32, message = "app.otp.hmac-secret must be at least 32 characters") String hmacSecret,
        @DefaultValue("5m") Duration expiry,
        @DefaultValue("60s") Duration resendCooldown,
        @DefaultValue("5") @Min(1) int maxSendAttempts,
        @DefaultValue("5") @Min(1) int maxVerifyAttempts,
        @DefaultValue("1h") Duration attemptWindow,
        @DefaultValue("30m") Duration blockDuration
) {
}
