package com.menzo.Identity_Service.Auth.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        Map<String, String> errors,
        Instant timestamp
) {

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(String code, String message, Map<String, String> errors) {
        Map<String, String> safe = (errors == null || errors.isEmpty()) ? null : errors;
        return new ApiResponse<>(false, code, message, null, safe, Instant.now());
    }
}
