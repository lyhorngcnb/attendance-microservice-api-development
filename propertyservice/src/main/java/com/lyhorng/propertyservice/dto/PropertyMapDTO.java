package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.PropertyMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyMapDTO {
    private Long id;
    private String mapData;
    private String color;
    private Double latitude;
    private Double longitude;
    private String attachment;
    private Long propertyId;
    private String propertyCode;  // Optional: include property code for display
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor from entity
    public PropertyMapDTO(PropertyMap propertyMap) {
        this.id = propertyMap.getId();
        this.mapData = propertyMap.getMapData();
        this.color = propertyMap.getColor();
        this.latitude = propertyMap.getLatitude();
        this.longitude = propertyMap.getLongitude();
        this.attachment = propertyMap.getAttachment();
        this.propertyId = propertyMap.getPropertyId();
        this.createdAt = propertyMap.getCreatedAt();
        this.updatedAt = propertyMap.getUpdatedAt();
        
        // Load property code if property is loaded (not lazy)
        if (propertyMap.getProperty() != null) {
            this.propertyCode = propertyMap.getProperty().getPropertyCode();
        }
    }
    
    // Constructor with property code
    public PropertyMapDTO(PropertyMap propertyMap, String propertyCode) {
        this(propertyMap);
        this.propertyCode = propertyCode;
    }
}