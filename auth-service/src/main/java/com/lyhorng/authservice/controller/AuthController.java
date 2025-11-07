// File: controller/AuthController.java
package com.lyhorng.authservice.controller;

import com.lyhorng.authservice.dto.LoginRequest;
import com.lyhorng.authservice.dto.LoginResponse;
import com.lyhorng.authservice.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for username: {}", loginRequest.getUsername());
        
        try {
            // Call authService which now returns complete LoginResponse
            LoginResponse response = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            LoginResponse response = LoginResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .token(null)
                    .username(null)
                    .userId(null)
                    .expiresIn(null)
                    .build();
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("Refresh token request received");
        
        try {
            // Extract token from "Bearer {token}" format
            String refreshToken = authorizationHeader.replace("Bearer ", "");
            String newAccessToken = authService.refreshToken(refreshToken);
            
            // Extract user info from the new token
            String username = authService.getJwtUtil().extractUsername(newAccessToken);
            Long userId = authService.getJwtUtil().extractUserId(newAccessToken);
            
            LoginResponse response = LoginResponse.builder()
                    .success(true)
                    .message("Token refreshed successfully")
                    .token(newAccessToken)
                    .username(username)
                    .userId(userId)
                    .expiresIn(authService.getJwtUtil().getExpirationTime())
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            LoginResponse response = LoginResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .token(null)
                    .username(null)
                    .userId(null)
                    .expiresIn(null)
                    .build();
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<LoginResponse> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("Token validation request received");
        
        try {
            // Extract token from "Bearer {token}" format
            String token = authorizationHeader.replace("Bearer ", "");
            boolean isValid = authService.validateToken(token);
            
            if (isValid) {
                String username = authService.getJwtUtil().extractUsername(token);
                Long userId = authService.getJwtUtil().extractUserId(token);
                
                LoginResponse response = LoginResponse.builder()
                        .success(true)
                        .message("Token is valid")
                        .token(token)
                        .username(username)
                        .userId(userId)
                        .expiresIn(authService.getJwtUtil().getExpirationTime())
                        .build();
                
                return ResponseEntity.ok(response);
            } else {
                LoginResponse response = LoginResponse.builder()
                        .success(false)
                        .message("Token is invalid")
                        .token(null)
                        .username(null)
                        .userId(null)
                        .expiresIn(null)
                        .build();
                
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            LoginResponse response = LoginResponse.builder()
                    .success(false)
                    .message("Token validation failed: " + e.getMessage())
                    .token(null)
                    .username(null)
                    .userId(null)
                    .expiresIn(null)
                    .build();
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}