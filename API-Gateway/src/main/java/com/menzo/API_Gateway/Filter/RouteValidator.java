package com.menzo.API_Gateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
//            "/auth/register",
//            "/auth/token",
//            "/user-register-form",
//            "/auth/admin-register",
//            "/favicon.ico"

            "/",
            "sign-in",
            "login",
            "/user/user-signin",
            "/auth/encode-pwd",
            "/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
