// ========================================
// 3. Enhanced JwtUtil.java
// Location: src/main/java/.../apigateway/security/JwtUtil.java
// ========================================
package com.rbc.demo.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;
    
    /**
     * Extract username from JWT token
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract user roles from JWT token
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            // Try to get roles first (preferred format)
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List) {
                return (List<String>) rolesObj;
            }
            
            // Fallback: handle authorities string format
            String authorities = claims.get("authorities", String.class);
            if (authorities != null && !authorities.isEmpty()) {
                // Convert authorities string to roles
                List<String> roles = new ArrayList<>();
                if (authorities.contains("ROLE_ADMIN")) {
                    roles.add("ADMIN");
                }
                if (authorities.contains("MANAGER") || authorities.contains("MANAGE")) {
                    roles.add("MANAGER");
                }
                if (roles.isEmpty()) {
                    roles.add("USER"); // Default role
                }
                return roles;
            }
            
            return List.of();
        } catch (Exception e) {
            log.error("Error extracting roles from token: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Extract user ID from JWT token
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            
            log.debug("JWT token is valid");
            return true;
            
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
            
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }
    
    /**
     * Get signing key from secret
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}