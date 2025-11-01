package com.lyhorng.propertyservice.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_unit_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUnitType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;  // e.g., "Square Meter", "Hectare", "Acre", "Square Foot"
    
    @Column(name = "code", length = 20)
    private String code;  // e.g., "m²", "ha", "ac", "ft²"
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "conversion_to_sqm", precision = 15, scale = 6)
    private java.math.BigDecimal conversionToSqm;  // Conversion factor to square meters
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}