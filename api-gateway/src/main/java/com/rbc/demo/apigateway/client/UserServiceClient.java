// ========================================
// 11. UserServiceClient.java (Feign Client Alternative using WebClient)
// Location: src/main/java/.../apigateway/client/UserServiceClient.java
// ========================================
package com.rbc.demo.apigateway.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(@Value("${user-service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    /**
     * Validate user credentials with User Service
     */
    public Mono<Boolean> validateUser(String username, String password) {
        log.debug("Validating user: {}", username);
        
        return webClient.post()
                .uri("/api/auth/validate")
                .bodyValue(new ValidationRequest(username, password))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnSuccess(result -> log.debug("User validation result: {}", result))
                .doOnError(error -> log.error("Error validating user: {}", error.getMessage()));
    }

    /**
     * Get user details from User Service
     */
    public Mono<UserDetailsResponse> getUserDetails(String username) {
        log.debug("Fetching user details for: {}", username);
        
        return webClient.get()
                .uri("/api/users/username/{username}", username)
                .retrieve()
                .bodyToMono(UserDetailsResponse.class)
                .doOnSuccess(user -> log.debug("Fetched user details: {}", user))
                .doOnError(error -> log.error("Error fetching user details: {}", error.getMessage()));
    }

    // DTOs
    private record ValidationRequest(String username, String password) {}
    
    public record UserDetailsResponse(
            Long id,
            String username,
            String email,
            boolean enabled,
            java.util.List<String> roles
    ) {}
}