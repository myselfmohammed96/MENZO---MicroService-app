package com.menzo.Identity_Service.Global.Exception;

import com.menzo.Identity_Service.Auth.Dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthException ex) {
        if (ex.getCode().getStatus().is5xxServerError()) {
            log.error("Auth operation failed [{}]: {}", ex.getCode(), ex.getMessage());
        } else {
            log.warn("Auth operation rejected [{}]: {}", ex.getCode(), ex.getMessage());
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(ex.getCode().getStatus());
        if (ex.getRetryAfterSeconds() != null) {
            builder.header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()));
        }
        return builder.body(ApiResponse.failure(ex.getCode().name(), ex.getMessage(), null));
    }

    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserService(UserServiceUnavailableException ex) {
        log.error("user-service unavailable", ex);
        return build(ErrorCode.USER_SERVICE_UNAVAILABLE, "Sign-in is temporarily unavailable. Please try again shortly.", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.putIfAbsent(fe.getField(), Objects.requireNonNullElse(fe.getDefaultMessage(), "Invalid value.")));
        log.warn("Validation failed for fields={}", errors.keySet());          // never log the rejected values
        return build(ErrorCode.VALIDATION_FAILED, "Validation failed.", errors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(Exception ex) {
        log.warn("Malformed request: {}", ex.getClass().getSimpleName());
        return build(ErrorCode.MALFORMED_REQUEST, "The request body is missing or malformed.", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return build(ErrorCode.METHOD_NOT_ALLOWED, "HTTP method not supported for this endpoint.", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return build(ErrorCode.RESOURCE_NOT_FOUND, "The requested resource was not found.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return build(ErrorCode.INTERNAL_ERROR, "Something went wrong on our side. Please try again later.", null);
    }

    private ResponseEntity<ApiResponse<Void>> build(ErrorCode code, String message, Map<String, String> errors) {
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.failure(code.name(), message, errors));
    }
}
