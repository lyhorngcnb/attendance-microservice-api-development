package com.lyhorng.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    
    // General Errors (1000-1999)
    INTERNAL_SERVER_ERROR("ERR_1000", "Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("ERR_1001", "Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("ERR_1002", "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("ERR_1003", "Access forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND("ERR_1004", "Resource not found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("ERR_1005", "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT("ERR_1006", "Resource conflict", HttpStatus.CONFLICT),
    UNSUPPORTED_MEDIA_TYPE("ERR_1007", "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    TOO_MANY_REQUESTS("ERR_1008", "Too many requests", HttpStatus.TOO_MANY_REQUESTS),
    SERVICE_UNAVAILABLE("ERR_1009", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    
    // Validation Errors (2000-2999)
    VALIDATION_ERROR("ERR_2000", "Validation error", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("ERR_2001", "Invalid input provided", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("ERR_2002", "Required field is missing", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("ERR_2003", "Invalid format", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE("ERR_2004", "Invalid date range", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("ERR_2005", "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE("ERR_2006", "Invalid phone number", HttpStatus.BAD_REQUEST),
    
    // Business Logic Errors (3000-3999)
    BUSINESS_ERROR("ERR_3000", "Business logic error", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTRY("ERR_3001", "Duplicate entry found", HttpStatus.CONFLICT),
    INSUFFICIENT_BALANCE("ERR_3002", "Insufficient balance", HttpStatus.BAD_REQUEST),
    OPERATION_NOT_ALLOWED("ERR_3003", "Operation not allowed", HttpStatus.FORBIDDEN),
    RESOURCE_LOCKED("ERR_3004", "Resource is locked", HttpStatus.LOCKED),
    QUOTA_EXCEEDED("ERR_3005", "Quota exceeded", HttpStatus.TOO_MANY_REQUESTS),
    
    // Data Errors (4000-4999)
    DATA_NOT_FOUND("ERR_4000", "Data not found", HttpStatus.NOT_FOUND),
    DATA_INTEGRITY_VIOLATION("ERR_4001", "Data integrity violation", HttpStatus.CONFLICT),
    DATABASE_ERROR("ERR_4002", "Database error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Authentication & Authorization Errors (5000-5999)
    INVALID_CREDENTIALS("ERR_5000", "Invalid credentials", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("ERR_5001", "Token has expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("ERR_5002", "Invalid token", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_PERMISSIONS("ERR_5003", "Insufficient permissions", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED("ERR_5004", "Account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED("ERR_5005", "Account is disabled", HttpStatus.FORBIDDEN),
    
    // External Service Errors (6000-6999)
    EXTERNAL_SERVICE_ERROR("ERR_6000", "External service error", HttpStatus.BAD_GATEWAY),
    EXTERNAL_SERVICE_TIMEOUT("ERR_6001", "External service timeout", HttpStatus.GATEWAY_TIMEOUT),
    EXTERNAL_SERVICE_UNAVAILABLE("ERR_6002", "External service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    
    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}