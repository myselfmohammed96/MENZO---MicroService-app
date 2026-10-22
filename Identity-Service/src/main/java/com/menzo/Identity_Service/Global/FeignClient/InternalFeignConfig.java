package com.menzo.Identity_Service.Global.FeignClient;

import com.menzo.Identity_Service.Global.Config.InternalProperties;
import com.menzo.Identity_Service.Global.Security.InternalApiKeyFilter;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class InternalFeignConfig {

    @Bean
    public RequestInterceptor internalKeyInterceptor(InternalProperties properties) {
        return template -> template.header(InternalApiKeyFilter.HEADER, properties.apiKey());
    }
}
