package com.lyhorng.customerservice.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First Name is required")  // Corrected typo
    @Column(nullable = false, length = 50)  // Removed unique constraint for firstName
    private String firstName;

    @NotBlank(message = "Last Name is required")  // Corrected typo
    @Column(nullable = false, length = 50)  // Removed unique constraint for lastName
    private String lastName;

    @NotBlank(message = "NID is required")  // Corrected typo
    @Column(nullable = false, unique = true, length = 50)  // Kept unique constraint for nid
    private String nid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
