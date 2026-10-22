package com.menzo.User_Service.Global.FeignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    public ErrorDecoder communicationErrorDecoder(ObjectMapper objectMapper) {
        return new CommunicationErrorDecoder(objectMapper);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

}
