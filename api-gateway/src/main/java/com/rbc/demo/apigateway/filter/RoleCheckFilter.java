// ========================================
// 13. RoleCheckFilter.java (Optional - For role-based access)
// Location: src/main/java/.../apigateway/filter/RoleCheckFilter.java
// ========================================
package com.rbc.demo.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class RoleCheckFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // Only check roles for protected endpoints
        if (!isProtectedEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        // Get required role from header (set by GatewayConfig)
        String requiredRole = request.getHeaders().getFirst("X-Required-Role");
        
        if (requiredRole == null) {
            // No specific role required, continue
            return chain.filter(exchange);
        }
        
        // Get user roles from header (set by AuthenticationFilter)
        String userRolesHeader = request.getHeaders().getFirst("X-User-Roles");
        
        if (userRolesHeader == null || userRolesHeader.isEmpty()) {
            log.warn("🚫 No user roles found for protected endpoint: {}", path);
            return onError(exchange, "Insufficient permissions", HttpStatus.FORBIDDEN);
        }
        
        List<String> userRoles = Arrays.asList(userRolesHeader.split(","));
        
        // Check if user has required role
        if (!userRoles.contains(requiredRole) && !userRoles.contains("ADMIN")) {
            log.warn("🚫 User does not have required role '{}' for: {}", requiredRole, path);
            return onError(exchange, "Insufficient permissions. Required role: " + requiredRole, HttpStatus.FORBIDDEN);
        }
        
        log.info("✅ Role check passed. User has role: {}", requiredRole);
        return chain.filter(exchange);
    }

    private boolean isProtectedEndpoint(String path) {
        return path.startsWith("/api/admin/") || 
               path.startsWith("/api/manager/") ||
               path.startsWith("/api/users/");
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        
        String errorResponse = String.format(
            "{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}",
            status.getReasonPhrase(),
            message,
            status.value()
        );
        
        return response.writeWith(Mono.just(
            response.bufferFactory().wrap(errorResponse.getBytes())
        ));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1; // Execute after authentication but before final processing
    }
}