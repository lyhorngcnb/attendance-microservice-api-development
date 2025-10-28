package com.lyhorng.common.constant;

public class Constants {
    
    private Constants() {
        throw new IllegalStateException("Constants class");
    }
    
    // API Constants
    public static final String API_VERSION_V1 = "/api/v1";
    public static final String API_VERSION_V2 = "/api/v2";
    
    // Header Constants
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BEARER_PREFIX = "Bearer ";
    public static final String HEADER_REQUEST_ID = "X-Request-ID";
    public static final String HEADER_API_KEY = "X-API-Key";
    public static final String HEADER_USER_ID = "X-User-ID";
    
    // Date Format Constants
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    // Status Constants
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_FAILED = "FAILED";
    
    // Pagination Constants
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    
    // Cache Constants
    public static final String CACHE_PREFIX = "app:cache:";
    public static final long CACHE_TTL_MINUTES = 30;
    public static final long CACHE_TTL_HOURS = 24;
    
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 20;
    
    // System Constants
    public static final String SYSTEM_USER = "SYSTEM";
    public static final String ANONYMOUS_USER = "ANONYMOUS";
    
    // Response Messages
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_CREATED = "Resource created successfully";
    public static final String MSG_UPDATED = "Resource updated successfully";
    public static final String MSG_DELETED = "Resource deleted successfully";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_BAD_REQUEST = "Invalid request";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access forbidden";
    public static final String MSG_INTERNAL_ERROR = "Internal server error";
}