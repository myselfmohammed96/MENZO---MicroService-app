package com.menzo.API_Gateway.Filter;

import com.menzo.API_Gateway.Dto.TokenDto;
import com.menzo.API_Gateway.Feign.GatewayFeign;
import com.menzo.API_Gateway.Util.JwtUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.Optional;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private GatewayFeign gatewayFeign;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            System.out.println("This is a normal trigger.......!");

            if (validator.isSecured.test(request)) {
                log.info("Secured request to: {}", path);
                Optional<String> jwtToken = extractJwtFromCookies(request);
//                System.out.println("This is what inside this token: " + jwtToken.get());

                if (jwtToken.isEmpty()) {
                    log.warn("JWT missing for secured path: {}", path);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing JWT in cookies");
                }

                try {
                    String token = jwtToken.get();
                    System.out.println("JWT found: " + token);
                    TokenDto tokenInDB;

                    try {
                        tokenInDB = gatewayFeign.getByToken(token);
                        System.out.println("Token found in DB: loggedOut = " + tokenInDB.isLoggedOut());
                    } catch (FeignException fe) {
                        System.out.println("FeignException: " + fe.status() + " - " + fe.getMessage());
                        System.out.println("X - FeignException while fetching token from DB: " + fe.getMessage());
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token validation service unavailable " + fe.getMessage());
                    }

                    if (tokenInDB.isLoggedOut()) {
//                        log.warn("Blocked! logged-out token for path: {}", path);
                        log.warn("Token is marked as logged out.");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token logged out");
                    }

                    jwtUtil.validateToken(token);
                    System.out.println("JWT is valid");

                    String email = jwtUtil.extractUserEmail(token);
                    String roles = jwtUtil.extractRoles(token);

                    System.out.println("Extracted Email: " + email);
                    System.out.println("Extracted Roles: " + roles);

                    request = request.mutate()
                            .header("loggedInUser", email)
                            .header("roles", roles)
                            .build();

                } catch (Exception e) {
                    System.out.println("X - JWT Validation failed: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                }
            } else {
                log.debug("Open(non-secured) request to: {}", path);
            }

            System.out.println("-> forwarding request downstream...");
            return chain.filter(exchange.mutate().request(request).build());
        };

    }

    private Optional<String> extractJwtFromCookies(ServerHttpRequest request) {
        request.getCookies().forEach((name, cookieList) ->
                System.out.println("cookie Found: " + name + " -> " + cookieList.get(0).getValue()));
        return Optional.ofNullable(request.getCookies().getFirst("JWT"))
                .map(cookie -> cookie.getValue());
    }

    public static class Config {
    }

}
