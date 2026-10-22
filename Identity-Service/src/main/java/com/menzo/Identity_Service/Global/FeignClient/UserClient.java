package com.menzo.Identity_Service.Global.FeignClient;

import com.menzo.Identity_Service.Auth.Dto.ApiResponse;
import com.menzo.Identity_Service.Auth.Dto.CredentialsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "USER-SERVICE",
        path = "/internal/v1/users",
        configuration = InternalFeignConfig.class
)
public interface UserClient {

    @GetMapping("/credentials")
    ApiResponse<CredentialsResponse> credentials(@RequestParam("email") String email);
}
