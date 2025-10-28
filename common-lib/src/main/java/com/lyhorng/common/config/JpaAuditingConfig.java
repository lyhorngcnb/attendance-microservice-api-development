package com.lyhorng.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Configuration for JPA Auditing to automatically populate
 * createdBy, updatedBy, createdAt, and updatedAt fields
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    /**
     * Default auditor aware implementation
     * Override this bean in your application to provide actual user context
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            // Default implementation returns SYSTEM
            // Override in your application to get actual user from SecurityContext
            return Optional.of("SYSTEM");
        }
    }
}

