package com.rbc.demo.apigateway.config;

import com.rbc.demo.apigateway.filter.AuthenticationFilter;
import com.rbc.demo.apigateway.filter.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Value("${user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    @Value("${auth-service.url:http://localhost:8082}")
    private String authServiceUrl;

    @Value("${customer-service.url:http://localhost:8083}")
    private String customerServiceUrl;

    @Value("${property-service.url:http://localhost:8084}")
    private String propertyServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // ==================== PUBLIC ROUTES ====================
                
                // Auth Service - Login (PUBLIC with strict rate limit)
                .route("auth-login-public", r -> r
                        .path("/api/auth/login", "/api/auth/register", "/api/auth/refresh")
                        .filters(f -> f
                                .filter(rateLimitFilter.apply(createRateLimitConfig(20, 60)))
                                .addRequestHeader("X-Route-Type", "Public-Auth")
                                .rewritePath("/api/auth/(?<segment>.*)", "/api/auth/$\\{segment}"))
                        .uri(authServiceUrl))
        
                
                // ==================== PROTECTED ROUTES ====================
                .route("user-service-protected", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .filter(rateLimitFilter.apply(createRateLimitConfig(100, 60)))
                                .addRequestHeader("X-Route-Type", "Protected-Users")
                                .addRequestHeader("X-Required-Role", "USER")
                                .rewritePath("/api/users/(?<segment>.*)", "/api/users/$\\{segment}"))
                        .uri(userServiceUrl))

                .route("customer-service-protected", r -> r
                        .path("/api/customers/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .filter(rateLimitFilter.apply(createRateLimitConfig(100, 60)))
                                .addRequestHeader("X-Route-Type", "Protected-Customers")
                                .addRequestHeader("X-Required-Role", "MANAGER")
                                .rewritePath("/api/customers/(?<segment>.*)", "/api/customers/$\\{segment}"))
                        .uri(customerServiceUrl))

                .route("property-service-protected", r -> r
                        .path("/api/properties/**", "/api/property/**")
                        .filters(f -> f
                                .filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
                                .filter(rateLimitFilter.apply(createRateLimitConfig(100, 60)))
                                .addRequestHeader("X-Route-Type", "Protected-Properties")
                                .addRequestHeader("X-Required-Role", "ADMIN")
                                .rewritePath("/api/properties/(?<segment>.*)", "/api/properties/$\\{segment}"))
                        .uri(propertyServiceUrl))
        
                // ==================== MONITORING ====================
                
                // Actuator (PUBLIC for monitoring - can be restricted in production)
                .route("actuator-public", r -> r
                        .path("/actuator/**")
                        .filters(f -> f.addRequestHeader("X-Route-Type", "Monitoring"))
                        .uri("http://localhost:8080"))
                
                .build();
    }

    /**
     * Helper method to create rate limit configuration
     */
    private RateLimitFilter.Config createRateLimitConfig(int maxRequests, int durationSeconds) {
        RateLimitFilter.Config config = new RateLimitFilter.Config();
        config.setMaxRequests(maxRequests);
        config.setDurationSeconds(durationSeconds);
        return config;
    }
}