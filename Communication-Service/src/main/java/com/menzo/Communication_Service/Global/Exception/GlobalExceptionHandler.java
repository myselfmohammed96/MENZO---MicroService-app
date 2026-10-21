package com.menzo.Communication_Service.Global.Exception;

import com.menzo.Communication_Service.Global.Dto.ApiResponse;
import com.menzo.Communication_Service.Global.Enum.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        if (ex.getCode().getStatus().is5xxServerError()) {
            logger.error("Request failed [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            logger.warn("Request rejected [{}]: {}", ex.getCode(), ex.getMessage());
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(ex.getCode().getStatus());
        if (ex.getRetryAfterSeconds() != null) {
            builder.header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()));
        }
        return builder.body(ApiResponse.failure(ex.getCode().name(), ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.putIfAbsent(fe.getField(), Objects.requireNonNullElse(fe.getDefaultMessage(), "Invalid value.")));
        logger.warn("Validation failed for fields={}", errors.keySet());
        return build(ErrorCode.VALIDATION_FAILED, "Validation failed.", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        logger.warn("Malformed request body: {}", ex.getMessage());
        return build(ErrorCode.MALFORMED_REQUEST, "The request body is missing or malformed.", null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return build(ErrorCode.UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json.", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return build(ErrorCode.METHOD_NOT_ALLOWED, "HTTP method not supported for this endpoint.", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return build(ErrorCode.RESOURCE_NOT_FOUND, "The requested resource was not found.", null);
    }

    /** First-ever OTP request for an email+purpose raced another one and lost on the unique constraint. */
    @ExceptionHandler({DataIntegrityViolationException.class, PessimisticLockingFailureException.class})
    public ResponseEntity<ApiResponse<Void>> handleConcurrency(Exception ex) {
        logger.warn("Concurrent OTP request detected: {}", ex.getMessage());
        return build(ErrorCode.CONCURRENT_REQUEST, "Another OTP request is already in progress. Please retry.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        logger.error("Unexpected error", ex);
        return build(ErrorCode.INTERNAL_ERROR, "Something went wrong on our side. Please try again later.", null);
    }

    private ResponseEntity<ApiResponse<Void>> build(ErrorCode code, String message, Map<String, String> errors) {
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.failure(code.name(), message, errors));
    }

}
