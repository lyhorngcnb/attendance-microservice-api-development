// ========================================
// 12. Enhanced GatewayConfig.java with Rate Limiting
// Location: src/main/java/.../apigateway/config/GatewayConfig.java (UPDATE)
// ========================================
package com.rbc.demo.apigateway.config;

import com.rbc.demo.apigateway.filter.AuthenticationFilter;
import com.rbc.demo.apigateway.filter.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                // ==================== PUBLIC ROUTES ====================

                                // Public Route: Authentication endpoints (no auth filter, with rate limit)
                                .route("user-auth-public", r -> r
                                                .path("/api/auth/login", "/api/auth/register", "/api/auth/refresh")
                                                .filters(f -> f
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(20, 60)))
                                                                .addRequestHeader("X-Route-Type", "Public-Auth"))
                                                .uri("http://localhost:8081"))

                                // Public Route: Password reset (with rate limit)
                                .route("user-password-reset", r -> r
                                                .path("/api/auth/forgot-password", "/api/auth/reset-password")
                                                .filters(f -> f
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(5, 300)))
                                                                .addRequestHeader("X-Route-Type",
                                                                                "Public-PasswordReset"))
                                                .uri("http://localhost:8081"))

                                // ==================== PROTECTED ROUTES ====================

                                // Protected Route: User Management (with authentication)
                                .route("user-service-protected", r -> r
                                                .path("/api/users/**")
                                                .filters(f -> f
                                                                .filter(authenticationFilter.apply(
                                                                                new AuthenticationFilter.Config()))
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(100, 60)))
                                                                .addRequestHeader("X-Route-Type", "Protected-Users"))
                                                .uri("http://localhost:8081"))

                                // Protected Route: Admin Dashboard (with authentication + role check)
                                .route("admin-service-protected", r -> r
                                                .path("/api/admin/**")
                                                .filters(f -> f
                                                                .filter(authenticationFilter.apply(
                                                                                new AuthenticationFilter.Config()))
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(50, 60)))
                                                                .addRequestHeader("X-Route-Type", "Protected-Admin")
                                                                .addRequestHeader("X-Required-Role", "ADMIN"))
                                                .uri("http://localhost:8081"))

                                // Protected Route: Manager Dashboard (with authentication)
                                .route("manager-service-protected", r -> r
                                                .path("/api/manager/**")
                                                .filters(f -> f
                                                                .filter(authenticationFilter.apply(
                                                                                new AuthenticationFilter.Config()))
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(75, 60)))
                                                                .addRequestHeader("X-Route-Type", "Protected-Manager")
                                                                .addRequestHeader("X-Required-Role", "MANAGER"))
                                                .uri("http://localhost:8081"))

                                // Protected Route: Product Service (with authentication)
                                .route("product-service-protected", r -> r
                                                .path("/api/products/**")
                                                .filters(f -> f
                                                                .filter(authenticationFilter.apply(
                                                                                new AuthenticationFilter.Config()))
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(100, 60)))
                                                                .addRequestHeader("X-Route-Type", "Protected-Products"))
                                                .uri("http://localhost:8083"))

                                // ==================== MONITORING ROUTES ====================

                                // Actuator endpoints (public for monitoring)
                                .route("actuator", r -> r
                                                .path("/actuator/**")
                                                .filters(f -> f.addRequestHeader("X-Route-Type", "Monitoring"))
                                                .uri("http://localhost:8080"))

                                .route("ped-service", r -> r
                                                .path("/api/v1/auth/login/**")
                                                .filters(f -> f
                                                                .filter(authenticationFilter.apply(
                                                                                new AuthenticationFilter.Config()))
                                                                .filter(rateLimitFilter
                                                                                .apply(createRateLimitConfig(100, 60)))
                                                                .addRequestHeader("X-Route-Type", "Protected-Products"))
                                                .uri("http://localhost:8085"))

                                .route("ped-docs", r -> r
                                                .path("/docs/**")
                                                .uri("http://localhost:8085"))

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
