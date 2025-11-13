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
 * UPDATED: Added missing fields: enabled, name, nid, phone
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    /**
     * NEW FIELD: Full name
     */
    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * NEW FIELD: National ID
     */
    @NotBlank(message = "NID is required")
    @Column(nullable = false, length = 255)
    private String nid;

    /**
     * NEW FIELD: Phone number
     */
    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone must be max 15 characters")
    @Column(nullable = false, length = 15)
    private String phone;

    /**
     * NEW FIELD: Account enabled status
     * Using Boolean wrapper (not primitive) to allow null checks and proper Lombok
     * getter
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * User status enum
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Additional constructor for creating User from parameters
    public User(String username, String email, String password, String firstName, String lastName,
            String name, String nid, String phone, Boolean enabled, UserStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.nid = nid;
        this.phone = phone;
        this.enabled = enabled;
        this.status = status;
    }

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}