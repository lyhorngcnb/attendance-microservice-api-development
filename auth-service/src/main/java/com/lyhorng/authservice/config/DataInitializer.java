// ========================================
// 8. Data Initializer
// ========================================

// File: config/DataInitializer.java
package com.lyhorng.authservice.config;

import com.lyhorng.authservice.model.User;
import com.lyhorng.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            
            // Create regular user
            User user = User.builder()
                    .username("user")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("user123"))
                    .firstName("Regular")
                    .lastName("User")
                    .enabled(true)
                    .build();
            userRepository.save(user);
            
            System.out.println("=================================");
            System.out.println("Auth Service - Test Users Created:");
            System.out.println("1. admin / admin123");
            System.out.println("2. user / user123");
            System.out.println("=================================");
        }
    }
}