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
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
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

            if (validator.isSecured.test(request)) {
                log.info("Secured request to: {}", path);
                Optional<String> jwtToken = extractJwtFromCookies(request);

                if (jwtToken.isEmpty()) {
                    log.warn("JWT missing for secured path: {}", path);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing JWT in cookies");
                }

                try {
                    String token = jwtToken.get();
                    log.info("JWT found for token: {}", token.substring(0, 21) + "...");
                    TokenDto tokenInDB;

                    try {
                        tokenInDB = gatewayFeign.getByToken(token);
                    } catch (FeignException fe) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token validation service unavailable " + fe.getMessage());
                    }

                    if (tokenInDB.isLoggedOut()) {
                        log.warn("Token is marked as logged out.");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token logged out");
                    }

                    if (!tokenInDB.isUserIsActive()) {
                        log.warn("User is marked as inActive");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You've been blocked. Your account is inactive..!");
                    }

                    jwtUtil.validateToken(token);
                    log.info("JWT validation successful");

                    String email = jwtUtil.extractUserEmail(token);
                    String roles = jwtUtil.extractRoles(token);

                    System.out.println(email);
                    request = request.mutate()
                            .header("loggedInUser", email)
                            .header("roles", roles)
                            .build();

                } catch (Exception e) {
                    log.warn("JWT validation failed: {}", e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                }
            } else {
                log.debug("Open(non-secured) request to: {}", path);
            }

            log.info("Forwarding request downstream ->");
            return chain.filter(exchange.mutate().request(request).build());
        };

    }

//    private Optional<String> extractJwtFromCookies(ServerHttpRequest request) {
//        log.info("Extracting JWT from cookie");
//        request.getCookies().forEach((name, cookieList) ->
//                System.out.println("cookie Found: " + name + " -> " + cookieList.get(0).getValue()));
//        return Optional.ofNullable(request.getCookies().getFirst("JWT"))
//                .map(cookie -> cookie.getValue());
//    }
    private Optional<String> extractJwtFromCookies(ServerHttpRequest request) {
        log.info("Extracting JWT from cookie");
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie jwtCookie = cookies.getFirst("JWT");

        if (jwtCookie == null) {
            log.warn("JWT cookie not found.");
            return Optional.empty();
        }
        log.info("JWT cookie found: {}", jwtCookie.getValue());
        return Optional.of(jwtCookie.getValue());
    }

    public static class Config {
    }

}
