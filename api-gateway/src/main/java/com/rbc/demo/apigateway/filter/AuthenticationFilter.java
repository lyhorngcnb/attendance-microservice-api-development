// ========================================
// 4. Enhanced AuthenticationFilter.java
// Location: src/main/java/.../apigateway/filter/AuthenticationFilter.java
// ========================================
package com.rbc.demo.apigateway.filter;

import com.rbc.demo.apigateway.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            log.info("🔍 Processing request: {} {}", request.getMethod(), request.getURI().getPath());
            
            // Skip authentication for public endpoints
            if (isPublicEndpoint(request.getURI().getPath())) {
                log.info("✅ Public endpoint - skipping authentication");
                return chain.filter(exchange);
            }
            
            // Check for Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.error("❌ Missing Authorization header");
                return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }
            
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                log.error("❌ Invalid Authorization header format");
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }
            
            String token = authHeader.substring(7);
            
            // Validate JWT token
            if (!jwtUtil.validateToken(token)) {
                log.error("❌ Invalid or expired JWT token");
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }
            
            // Extract user information
            String username = jwtUtil.getUsernameFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<String> roles = jwtUtil.getRolesFromToken(token);
            
            log.info("✅ Authenticated user: {} (ID: {}, Roles: {})", username, userId, roles);
            
            // Add user information to request headers for downstream services
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Roles", String.join(",", roles))
                    .header("X-Authenticated", "true")
                    .build();
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }
    
    /**
     * Check if endpoint is public (doesn't require authentication)
     */
    private boolean isPublicEndpoint(String path) {
        return path.contains("/auth/") ||
               path.contains("/actuator/") ||
               path.contains("/h2-console/") ||
               path.equals("/") ||
               path.contains("/swagger") ||
               path.contains("/api-docs");
    }
    
    /**
     * Handle authentication errors
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        
        log.error("🚫 Authentication Error: {}", err);
        
        String errorResponse = String.format(
            "{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}",
            httpStatus.getReasonPhrase(),
            err,
            httpStatus.value()
        );
        
        return response.writeWith(Mono.just(
            response.bufferFactory().wrap(errorResponse.getBytes())
        ));
    }
    
    public static class Config {
        // Configuration properties if needed in the future
        private boolean requireAuthentication = true;
        
        public boolean isRequireAuthentication() {
            return requireAuthentication;
        }
        
        public void setRequireAuthentication(boolean requireAuthentication) {
            this.requireAuthentication = requireAuthentication;
        }
    }
}