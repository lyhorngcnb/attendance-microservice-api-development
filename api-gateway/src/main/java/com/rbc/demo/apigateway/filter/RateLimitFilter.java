// ========================================
// 6. RateLimitFilter.java (Optional - for rate limiting)
// Location: src/main/java/.../apigateway/filter/RateLimitFilter.java
// ========================================
package com.rbc.demo.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {

    // Simple in-memory rate limiting (for production, use Redis)
    private final Map<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    public RateLimitFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientId = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            
            RateLimitInfo info = rateLimitMap.computeIfAbsent(clientId, 
                k -> new RateLimitInfo(config.getMaxRequests(), config.getDurationSeconds()));
            
            if (info.isAllowed()) {
                log.debug("Rate limit check passed for: {}", clientId);
                return chain.filter(exchange);
            } else {
                log.warn("Rate limit exceeded for: {}", clientId);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().add("X-RateLimit-Limit", String.valueOf(config.getMaxRequests()));
                exchange.getResponse().getHeaders().add("X-RateLimit-Remaining", "0");
                exchange.getResponse().getHeaders().add("X-RateLimit-Reset", String.valueOf(info.getResetTime()));
                
                String errorMessage = "{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please try again later.\"}";
                return exchange.getResponse().writeWith(Mono.just(
                    exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes())
                ));
            }
        };
    }

    public static class Config {
        private int maxRequests = 100; // Max requests per duration
        private int durationSeconds = 60; // Duration in seconds

        public int getMaxRequests() {
            return maxRequests;
        }

        public void setMaxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
        }

        public int getDurationSeconds() {
            return durationSeconds;
        }

        public void setDurationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
        }
    }

    private static class RateLimitInfo {
        private final AtomicInteger count;
        private final long resetTime;
        private final int maxRequests;
        private final int durationSeconds;

        public RateLimitInfo(int maxRequests, int durationSeconds) {
            this.maxRequests = maxRequests;
            this.durationSeconds = durationSeconds;
            this.count = new AtomicInteger(0);
            this.resetTime = System.currentTimeMillis() + Duration.ofSeconds(durationSeconds).toMillis();
        }

        public boolean isAllowed() {
            long currentTime = System.currentTimeMillis();
            
            if (currentTime > resetTime) {
                // Reset counter
                count.set(0);
                return true;
            }
            
            return count.incrementAndGet() <= maxRequests;
        }

        public long getResetTime() {
            return resetTime;
        }
    }
}