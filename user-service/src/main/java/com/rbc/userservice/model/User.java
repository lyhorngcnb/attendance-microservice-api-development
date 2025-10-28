package com.rbc.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Entity - Represents the 'users' table in database
 * 
 * ANNOTATIONS EXPLAINED:
 * @Entity - Marks this class as a JPA entity (database table)
 * @Table - Specifies table name in database
 * @Data - Lombok: generates getters, setters, toString, equals, hashCode
 * @NoArgsConstructor - Lombok: generates no-argument constructor
 * @AllArgsConstructor - Lombok: generates constructor with all fields
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /**
     * Primary Key Configuration
     * @Id - Marks as primary key
     * @GeneratedValue - Auto-generates value
     * IDENTITY strategy - Uses database auto_increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Username field with validation
     * @NotBlank - Cannot be null or empty
     * @Size - Length must be between 3-50 characters
     * @Column - Database column configuration
     *   - nullable=false: NOT NULL constraint
     *   - unique=true: UNIQUE constraint
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * Email field with validation
     * @Email - Validates email format
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * Password field
     * Note: In production, passwords should be encrypted (BCrypt)
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;
    
    /**
     * First and Last name
     */
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    /**
     * User status
     * @Enumerated - Stores enum as string in database
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    /**
     * Automatic timestamp fields
     * @CreationTimestamp - Auto-sets on creation
     * @UpdateTimestamp - Auto-updates on modification
     * updatable=false - Creation time never changes
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * User status enum
     */
    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}