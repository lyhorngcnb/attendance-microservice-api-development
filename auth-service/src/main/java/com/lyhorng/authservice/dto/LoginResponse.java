package com.lyhorng.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    
    @Builder.Default
    private String token = null;
    
    // If you want to include additional user information
    private String username;
    private Long userId;
    
    // If you want to include token expiration info
    private Long expiresIn;
}