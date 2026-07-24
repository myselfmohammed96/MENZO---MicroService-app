package com.menzo.User_Service.DataSeeder.DesignationSeeder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("designations")
@Data
public class DesignationProperties {

    private List<String> core;

}
