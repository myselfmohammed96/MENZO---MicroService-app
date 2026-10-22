package com.menzo.User_Service.Global.FeignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class IdentityFeignConfig {

    @Bean
    public ErrorDecoder identityErrorDecoder(ObjectMapper objectMapper) {
        return new IdentityErrorDecoder(objectMapper);
    }

    @Bean
    public Retryer identityRetryer() {
        return Retryer.NEVER_RETRY;
    }
}
