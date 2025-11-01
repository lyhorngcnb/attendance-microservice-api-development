package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.PropertyLand;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyLandDTO {
    private Long id;
    private BigDecimal landSize;
    private BigDecimal front;
    private BigDecimal back;
    private BigDecimal length;
    private BigDecimal width;
    private String dimensionLand;
    private String lotNumber;
    private Long propertyUnitTypeId;
    private String propertyUnitTypeName;
    private String propertyUnitTypeCode;
    private Long propertyId;
    private String propertyCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor from entity
    public PropertyLandDTO(PropertyLand propertyLand) {
        this.id = propertyLand.getId();
        this.landSize = propertyLand.getLandSize();
        this.front = propertyLand.getFront();
        this.back = propertyLand.getBack();
        this.length = propertyLand.getLength();
        this.width = propertyLand.getWidth();
        this.dimensionLand = propertyLand.getDimensionLand();
        this.lotNumber = propertyLand.getLotNumber();
        this.propertyUnitTypeId = propertyLand.getPropertyUnitTypeId();
        this.propertyId = propertyLand.getPropertyId();
        this.createdAt = propertyLand.getCreatedAt();
        this.updatedAt = propertyLand.getUpdatedAt();
    }
}