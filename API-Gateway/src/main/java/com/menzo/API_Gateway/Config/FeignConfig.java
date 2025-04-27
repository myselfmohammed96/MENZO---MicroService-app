package com.menzo.API_Gateway.Config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public HttpMessageConverters feignHttpMessageConverters(){
        return new HttpMessageConverters();
    }
}
