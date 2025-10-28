// ========================================
// 7. LoggingFilter.java (Request/Response Logging)
// Location: src/main/java/.../apigateway/filter/LoggingFilter.java
// ========================================
package com.rbc.demo.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
        String path = request.getURI().getPath();
        String clientIp = request.getRemoteAddress() != null 
            ? request.getRemoteAddress().getAddress().getHostAddress() 
            : "unknown";
        
        log.info("📥 [{}] {} {} from {}", timestamp, method, path, clientIp);
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("📤 [{}] {} {} - Status: {} - Duration: {}ms", 
                timestamp, method, path, 
                response.getStatusCode(), 
                duration);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // Execute first
    }
}