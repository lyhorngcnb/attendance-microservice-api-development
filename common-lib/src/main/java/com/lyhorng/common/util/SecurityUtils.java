package com.lyhorng.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for security-related operations
 */
public class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get current authenticated username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "ANONYMOUS";
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return principal.toString();
    }

    /**
     * Check if current user is authenticated
     */
    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * Check if current user has a specific authority/role
     */
    public static boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    /**
     * Get current authentication object
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Clear security context
     */
    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }
}

