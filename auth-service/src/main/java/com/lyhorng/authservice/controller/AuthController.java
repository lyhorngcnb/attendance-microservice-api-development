// File: controller/AuthController.java
package com.lyhorng.authservice.controller;

import com.lyhorng.authservice.dto.JwtResponse;
import com.lyhorng.authservice.dto.LoginRequest;
import com.lyhorng.authservice.dto.RegisterRequest;
import com.lyhorng.authservice.service.AuthService;
import com.lyhorng.authservice.service.AuthService.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestHeader("Authorization") String token) {
        JwtResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}
