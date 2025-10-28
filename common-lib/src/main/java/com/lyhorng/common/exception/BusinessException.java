package com.lyhorng.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private final String details;
    private final Object[] args;
    
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
        this.args = null;
    }
    
    public BusinessException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
        this.args = null;
    }
    
    public BusinessException(ErrorCode errorCode, String details, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
        this.args = args;
    }
    
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = null;
        this.args = null;
    }
    
    public BusinessException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = details;
        this.args = null;
    }
    
    public BusinessException(String message) {
        super(message);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
        this.details = message;
        this.args = null;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
        this.details = message;
        this.args = null;
    }
}