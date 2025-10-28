package com.lyhorng.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import com.lyhorng.common.exception.BusinessException;
import com.lyhorng.common.exception.ErrorCode;

public class ValidationUtils {
    
    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void notNull(Object object, ErrorCode errorCode) {
        if (object == null) {
            throw new BusinessException(errorCode);
        }
    }
    
    public static void notNull(Object object, Supplier<BusinessException> exceptionSupplier) {
        if (object == null) {
            throw exceptionSupplier.get();
        }
    }
    
    public static void notEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void notEmpty(String str, ErrorCode errorCode) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(errorCode);
        }
    }
    
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void notEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(errorCode);
        }
    }
    
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new BusinessException(errorCode);
        }
    }
    
    public static void isTrue(boolean expression, Supplier<BusinessException> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }
    
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void isFalse(boolean expression, ErrorCode errorCode) {
        if (expression) {
            throw new BusinessException(errorCode);
        }
    }
    
    public static void equals(Object obj1, Object obj2, String message) {
        if (obj1 == null && obj2 == null) {
            return;
        }
        if (obj1 == null || !obj1.equals(obj2)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void notEquals(Object obj1, Object obj2, String message) {
        if (obj1 != null && obj1.equals(obj2)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void inRange(int value, int min, int max, String message) {
        if (value < min || value > max) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void inRange(long value, long min, long max, String message) {
        if (value < min || value > max) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void positive(int value, String message) {
        if (value <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void positive(long value, String message) {
        if (value <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void nonNegative(int value, String message) {
        if (value < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void nonNegative(long value, String message) {
        if (value < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, message);
        }
    }
    
    public static void validEmail(String email, String message) {
        if (!StringUtils.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL, message);
        }
    }
    
    public static void validPhone(String phone, String message) {
        if (!StringUtils.isValidPhone(phone)) {
            throw new BusinessException(ErrorCode.INVALID_PHONE, message);
        }
    }
}