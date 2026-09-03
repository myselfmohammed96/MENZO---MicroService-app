package com.menzo.Home_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ConfigurationPropertiesScan
public class ViewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViewServiceApplication.class, args);
	}

}
