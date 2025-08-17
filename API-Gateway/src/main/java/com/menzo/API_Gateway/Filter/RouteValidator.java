package com.menzo.API_Gateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(

//            "/",
            "/index",
            "/all-categories",
            "/login",
            "/logout",
            "/sign-in",
            "/user/user-signin",
            "/auth/encode-pwd",
            "/auth/login",

        //  static end points
            "/home-css/",
            "/home-js/",
            "/home-media/",
            "/css/",
            "/js/",
            "/media/"
//            "/admin/categories"
//            "/categories/health-check"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));
}
