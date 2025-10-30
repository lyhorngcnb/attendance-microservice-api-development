// File: service/AuthService.java
package com.lyhorng.authservice.service;

import com.lyhorng.authservice.dto.JwtResponse;
import com.lyhorng.authservice.dto.LoginRequest;
import com.lyhorng.authservice.dto.RegisterRequest;
import com.lyhorng.authservice.model.User;
import com.lyhorng.authservice.repository.UserRepository;
import com.lyhorng.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public ApiResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already exists");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .build();
        
        userRepository.save(user);
        return new ApiResponse(true, "User registered successfully");
    }
    
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        if (!user.getEnabled()) {
            throw new RuntimeException("Account is disabled");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        
        return new JwtResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
    
    public JwtResponse refreshToken(String oldToken) {
        String token = oldToken.substring(7); // Remove "Bearer "
        String username = jwtUtil.getUsernameFromToken(token);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String newToken = jwtUtil.generateToken(user.getUsername(), user.getId());
        
        return new JwtResponse(
                newToken,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
    
    // Inner class for ApiResponse (if using common package)
    // Or create separate file in dto package
    public static class ApiResponse {
        private Boolean success;
        private String message;
        
        public ApiResponse(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
    }
}