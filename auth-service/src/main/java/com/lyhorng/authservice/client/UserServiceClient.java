package com.lyhorng.authservice.client;

import com.lyhorng.authservice.dto.UserDTO;
import com.lyhorng.authservice.dto.UserServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(@Value("${user-service.url:http://localhost:8081}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public List<UserDTO> getAllUsers() {
        try {
            log.debug("Fetching users from user service");
            
            UserServiceResponse response = webClient.get()
                    .uri("/api/users")
                    .retrieve()
                    .bodyToMono(UserServiceResponse.class)
                    .block();

            if (response != null && response.isSuccess() && response.getData() != null) {
                log.debug("Successfully fetched {} users", response.getData().size());
                return response.getData();
            }
            
            log.warn("User service returned empty or unsuccessful response");
            return Collections.emptyList();
            
        } catch (WebClientResponseException e) {
            log.error("Error fetching users from user service. Status: {}, Message: {}", 
                     e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Failed to fetch users from user service: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching users from user service: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while fetching users", e);
        }
    }

    public UserDTO getUserByUsername(String username) {
        log.debug("Searching for user with username: {}", username);
        
        try {
            List<UserDTO> users = getAllUsers();
            return users.stream()
                    .filter(user -> username.equals(user.getUsername()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error searching for user by username {}: {}", username, e.getMessage());
            return null;
        }
    }

    public UserDTO getUserById(Long id) {
        log.debug("Searching for user with id: {}", id);
        
        try {
            List<UserDTO> users = getAllUsers();
            return users.stream()
                    .filter(user -> id.equals(user.getId()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error searching for user by id {}: {}", id, e.getMessage());
            return null;
        }
    }
}