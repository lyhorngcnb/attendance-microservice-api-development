package com.lyhorng.common.util;

import java.util.Collection;
import java.util.regex.Pattern;

public class StringUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[0-9]{10,15}$"
    );
    
    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }
    
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }
    
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }
    
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        
        for (char c : str.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
    
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone.replaceAll("[\\s()-]", "")).matches();
    }
    
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String masked = username.length() > 3 
                ? username.substring(0, 2) + "***" + username.substring(username.length() - 1)
                : "***";
        return masked + "@" + parts[1];
    }
    
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 4) {
            return phone;
        }
        String digits = phone.replaceAll("[^0-9]", "");
        return "***" + digits.substring(digits.length() - 4);
    }
    
    public static String join(Collection<?> collection, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        return String.join(delimiter, collection.stream()
                .map(Object::toString)
                .toArray(String[]::new));
    }
    
    public static String removeWhitespace(String str) {
        return str != null ? str.replaceAll("\\s+", "") : null;
    }
    
    public static String normalizeWhitespace(String str) {
        return str != null ? str.replaceAll("\\s+", " ").trim() : null;
    }
}