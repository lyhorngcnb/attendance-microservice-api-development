// ========================================
// 9. GatewayProperties.java (Custom Configuration Properties)
// Location: src/main/java/.../apigateway/config/GatewayProperties.java
// ========================================
package com.rbc.demo.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gateway.security")
@Data
public class GatewayProperties {
    
    private List<String> publicEndpoints;
    private List<String> adminEndpoints;
    private List<String> managerEndpoints;
    private boolean enableRateLimiting = false;
    private int rateLimitMaxRequests = 100;
    private int rateLimitDurationSeconds = 60;
}