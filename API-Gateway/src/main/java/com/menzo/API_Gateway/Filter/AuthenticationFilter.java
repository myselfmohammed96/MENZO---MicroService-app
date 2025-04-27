package com.menzo.API_Gateway.Filter;

import com.menzo.API_Gateway.Dto.TokenDto;
import com.menzo.API_Gateway.Feign.GatewayFeign;
import com.menzo.API_Gateway.Util.JwtUtil;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private GatewayFeign gatewayFeign;

    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(validator.isSecured.test(request)){
                Optional<String> jwtToken = extractJwtFromCookies(request);
                if(jwtToken.isEmpty()){
                    throw new RuntimeException("Missing JWT in cookies");
                }
                try {
                    String token = jwtToken.get();
                    TokenDto tokenInDB;
                    try {
                        tokenInDB = gatewayFeign.getByToken(token);
                    } catch (FeignException e) {
                        System.out.println("FeignException: " + e.status() + " - " + e.getMessage());
                        throw new RuntimeException("Feign client failed: " + e.getMessage());
                    }
                    if (tokenInDB.isLoggedOut()) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
                    }
                    jwtUtil.validateToken(token);
                    request = exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", jwtUtil.extractUserEmail(token))
                            .header("roles", jwtUtil.extractRoles(token))
                            .build();
                } catch (Exception e){
                    System.out.println("Invalid JWT token!");
                    throw new RuntimeException("Unauthorized access");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());

        };
    }

    private Optional<String> extractJwtFromCookies(ServerHttpRequest request){
        request.getCookies().forEach((name, cookieList) ->
                System.out.println("cookie Found: " + name + " -> " + cookieList.get(0).getValue()));
        return Optional.ofNullable(request.getCookies().getFirst("JWT"))
                .map(cookie -> cookie.getValue());
    }

    public static class Config{}
}
