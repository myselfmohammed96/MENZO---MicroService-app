package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Auth.Dto.ApiResponse;
import com.menzo.Identity_Service.Auth.Dto.CredentialsResponse;
import com.menzo.Identity_Service.Global.Exception.UserServiceUnavailableException;
import com.menzo.Identity_Service.Global.FeignClient.UserClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialsGateway {

    private final UserClient client;

    public Optional<CredentialsResponse> find(String email) {
        try {
            ApiResponse<CredentialsResponse> response = client.credentials(email);
            return Optional.ofNullable(response == null ? null : response.data());
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        } catch (RuntimeException ex) {                 // timeouts, 5xx, no Eureka instance, bad JSON...
            log.error("Could not load credentials from user-service", ex);
            throw new UserServiceUnavailableException(ex);
        }
    }
}
