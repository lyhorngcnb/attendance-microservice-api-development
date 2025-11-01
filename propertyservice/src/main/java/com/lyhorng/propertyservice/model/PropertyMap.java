package com.lyhorng.propertyservice.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "property_maps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyMap {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "map_data", columnDefinition = "TEXT")
    private String mapData;  // Can store GeoJSON or coordinates
    
    @Column(name = "color", length = 50)
    private String color;  // Hex color code like #FF5733
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "attachment", length = 500)
    private String attachment;  // File path or URL (can be null)
    
    // Relationship to Property
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;
    
    // Helper column to expose property ID without loading relationship
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