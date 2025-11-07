package com.lyhorng.authservice.service;

import com.lyhorng.authservice.client.UserServiceClient;
import com.lyhorng.authservice.dto.LoginResponse;
import com.lyhorng.authservice.dto.UserDTO;
import com.lyhorng.authservice.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    
    private final UserServiceClient userServiceClient;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    public AuthService(UserServiceClient userServiceClient, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userServiceClient = userServiceClient;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    
    public LoginResponse login(String username, String password) {
        log.info("Login attempt for username: {}", username);
        
        // Get user from user service
        UserDTO user = userServiceClient.getUserByUsername(username);
        
        if (user == null) {
            log.warn("User not found: {}", username);
            throw new RuntimeException("Invalid username or password");
        }
        
        // Check if user is active
        if (!"ACTIVE".equals(user.getStatus())) {
            log.warn("User account is not active: {}", username);
            throw new RuntimeException("User account is not active");
        }
        
        // Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password for user: {}", username);
            throw new RuntimeException("Invalid username or password");
        }
        
        log.info("User authenticated successfully: {}", username);
        
        // Generate access token
        String token = jwtUtil.generateAccessToken(username, user.getId());
        
        // Return complete LoginResponse with all user information
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .expiresIn(jwtUtil.getExpirationTime())
                .build();
    }

    public String refreshToken(String refreshToken) {
        log.info("Refreshing token");
        
        // Validate the refresh token
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        // Extract username from refresh token
        String username = jwtUtil.extractUsername(refreshToken);
        Long userId = jwtUtil.extractUserId(refreshToken);
        
        // Generate new access token
        return jwtUtil.generateAccessToken(username, userId);
    }

    public boolean validateToken(String token) {
        try {
            // Extract username from token
            String username = jwtUtil.extractUsername(token);
            
            // Get user to validate
            UserDTO user = userServiceClient.getUserByUsername(username);
            if (user == null) {
                return false;
            }
            
            // Validate token with username
            return jwtUtil.validateToken(token, username);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Add this getter method
    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}