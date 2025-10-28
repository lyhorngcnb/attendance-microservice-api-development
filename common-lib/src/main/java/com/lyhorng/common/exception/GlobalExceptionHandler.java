package com.lyhorng.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.common.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        log.error("Business exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .details(ex.getDetails())
                .status(ex.getErrorCode().getHttpStatus().value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message(ex.getErrorCode().getMessage())
                .data(errorResponse)
                .errorCode(ex.getErrorCode().getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.error("Validation exception occurred: {}", ex.getMessage());
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError ? 
                            ((FieldError) error).getField() : error.getObjectName();
                    String message = error.getDefaultMessage();
                    Object rejectedValue = error instanceof FieldError ? 
                            ((FieldError) error).getRejectedValue() : null;
                    
                    return ErrorResponse.FieldError.builder()
                            .field(fieldName)
                            .message(message)
                            .rejectedValue(rejectedValue)
                            .build();
                })
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Validation failed")
                .data(errorResponse)
                .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        log.error("Constraint violation exception occurred: {}", ex.getMessage());
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> ErrorResponse.FieldError.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .rejectedValue(violation.getInvalidValue())
                        .build())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                .message("Constraint violation")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Constraint violation")
                .data(errorResponse)
                .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        log.error("HTTP message not readable exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_FORMAT.getCode())
                .message("Invalid request body format")
                .details(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Invalid request body format")
                .data(errorResponse)
                .errorCode(ErrorCode.INVALID_FORMAT.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        log.error("Missing request parameter exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.MISSING_REQUIRED_FIELD.getCode())
                .message(String.format("Required parameter '%s' is missing", ex.getParameterName()))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Missing required parameter")
                .data(errorResponse)
                .errorCode(ErrorCode.MISSING_REQUIRED_FIELD.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        log.error("Method argument type mismatch exception: {}", ex.getMessage());
        
        String message = String.format("Invalid value '%s' for parameter '%s'", 
                ex.getValue(), ex.getName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Invalid parameter type")
                .data(errorResponse)
                .errorCode(ErrorCode.INVALID_INPUT.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        log.error("HTTP method not supported exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED.getCode())
                .message(String.format("Method '%s' is not supported", ex.getMethod()))
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Method not allowed")
                .data(errorResponse)
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        log.error("No handler found exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.NOT_FOUND.getCode())
                .message("Endpoint not found")
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Endpoint not found")
                .data(errorResponse)
                .errorCode(ErrorCode.NOT_FOUND.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message("Internal server error")
                .data(errorResponse)
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}