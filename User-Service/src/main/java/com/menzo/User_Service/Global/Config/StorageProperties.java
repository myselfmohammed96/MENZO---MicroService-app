package com.menzo.User_Service.Global.Config;

import jakarta.ws.rs.DefaultValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        @DefaultValue("./uploads/profile") Path profileDir,
        @DefaultValue("/uploads/profile") String publicUrlPrefix,
        @DefaultValue("2MB") DataSize maxProfilePicSize
) {
}
