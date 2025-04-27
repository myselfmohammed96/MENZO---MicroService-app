package com.menzo.API_Gateway.Feign;

import com.menzo.API_Gateway.Config.FeignConfig;
import com.menzo.API_Gateway.Dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "IDENTITY-SERVICE", url = "http://localhost:9898", configuration = FeignConfig.class)
public interface GatewayFeign {

    @GetMapping("/auth/get-by-token")
    public TokenDto getByToken(@RequestParam String token);
}
