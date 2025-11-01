package com.lyhorng.propertyservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "property_lands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyLand {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "land_size", precision = 15, scale = 2)
    private BigDecimal landSize;  // Total land size
    
    @Column(name = "front", precision = 10, scale = 2)
    private BigDecimal front;  // Front measurement
    
    @Column(name = "back", precision = 10, scale = 2)
    private BigDecimal back;  // Back measurement
    
    @Column(name = "length", precision = 10, scale = 2)
    private BigDecimal length;  // Length measurement
    
    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;  // Width measurement
    
    @Column(name = "dimension_land", length = 255)
    private String dimensionLand;  // e.g., "20m x 30m" or custom description
    
    @Column(name = "lot_number", length = 100)
    private String lotNumber;  // Plot/Lot number
    
    // Relationship to PropertyUnitType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_type_id")
    @JsonIgnore
    private PropertyUnitType propertyUnitType;
    
    @Column(name = "property_unit_type_id", insertable = false, updatable = false)
    private Long propertyUnitTypeId;
    
    // Relationship to Property
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;
    
    @Column(name = "property_id", insertable = false, updatable = false)
    private Long propertyId;
    
    // Audit fields
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