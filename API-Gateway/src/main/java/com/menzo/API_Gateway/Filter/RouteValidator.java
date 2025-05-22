package com.menzo.API_Gateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(

//            "/",
            "/sign-in",
            "/login",
            "/user/user-signin",
            "/auth/encode-pwd",
            "/auth/login"
//            "/admin/categories"
//            "/categories/health-check"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
//                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));
//                    && !request.getURI().getPath().matches(".+\\.(css|js|png|jpg|jpeg|gif|svg|woff2?|ttf|eot)$");
}
