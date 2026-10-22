package com.menzo.User_Service.Global.Exception;

import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, String> BINDING_HINTS = Map.of(
            "dateOfBirth", "Date of birth must be a valid date in dd-MM-yyyy format.",
            "gender", "Invalid gender value."
    );

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        if (ex.getCode().getStatus().is5xxServerError()) {
            log.error("Request failed [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.warn("Request rejected [{}]: {}", ex.getCode(), ex.getMessage());
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(ex.getCode().getStatus());
        if (ex.getRetryAfterSeconds() != null) {
            builder.header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()));
        }
        return builder.body(ApiResponse.failure(ex.getCode().name(), ex.getMessage(), ex.getErrors()));
    }

    /** Covers MethodArgumentNotValidException too (it extends BindException), incl. type-mismatch errors. */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBind(org.springframework.validation.BindException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fe.getField(), resolveMessage(fe));   // never echo rejected values (passwords!)
        }
        ex.getBindingResult().getGlobalErrors()
                .forEach(ge -> errors.putIfAbsent(ge.getObjectName(), Objects.requireNonNullElse(ge.getDefaultMessage(), "Invalid request.")));
        log.warn("Validation failed for fields={}", errors.keySet());
        return build(ErrorCode.VALIDATION_FAILED, "Validation failed. Please correct the highlighted fields.", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v -> errors.putIfAbsent(v.getPropertyPath().toString(), v.getMessage()));
        log.warn("Constraint violation on {}", errors.keySet());
        return build(ErrorCode.VALIDATION_FAILED, "Validation failed. Please correct the highlighted fields.", errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleTooLarge(MaxUploadSizeExceededException ex) {
        log.warn("Upload rejected, size limit exceeded: {}", ex.getMessage());
        return build(ErrorCode.FILE_TOO_LARGE, "The uploaded file is too large.", null);
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponse<Void>> handleMissingPart(Exception ex) {
        log.warn("Missing request part/parameter: {}", ex.getMessage());
        return build(ErrorCode.MALFORMED_REQUEST, "The request is missing required data.", null);
    }

    @ExceptionHandler({MultipartException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleMalformed(Exception ex) {
        log.warn("Malformed request: {}", ex.getMessage());
        return build(ErrorCode.MALFORMED_REQUEST, "The request could not be read. Please send valid multipart/form-data.", null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        log.warn("Unsupported media type: {}", ex.getContentType());
        return build(ErrorCode.UNSUPPORTED_MEDIA_TYPE, "Content type must be multipart/form-data.", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethod(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not allowed: {}", ex.getMethod());
        return build(ErrorCode.METHOD_NOT_ALLOWED, "HTTP method not supported for this endpoint.", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        log.warn("Resource not found: {}", ex.getResourcePath());
        return build(ErrorCode.RESOURCE_NOT_FOUND, "The requested resource was not found.", null);
    }

    /** Safety net for the unique-constraint race (two identical signups at the same instant). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return build(ErrorCode.USER_ALREADY_EXISTS, "An account with these details already exists.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return build(ErrorCode.INTERNAL_ERROR, "Something went wrong on our side. Please try again later.", null);
    }

    private String resolveMessage(FieldError fe) {
        if (fe.isBindingFailure()) {
            return BINDING_HINTS.getOrDefault(fe.getField(), "Invalid value for '" + fe.getField() + "'.");
        }
        return Objects.requireNonNullElse(fe.getDefaultMessage(), "Invalid value.");
    }

    private ResponseEntity<ApiResponse<Void>> build(ErrorCode code, String message, Map<String, String> errors) {
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.failure(code.name(), message, errors));
    }

}
