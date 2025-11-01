package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.PropertyUnitType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUnitTypeDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private BigDecimal conversionToSqm;
    private Boolean isActive;
    
    public PropertyUnitTypeDTO(PropertyUnitType unitType) {
        this.id = unitType.getId();
        this.name = unitType.getName();
        this.code = unitType.getCode();
        this.description = unitType.getDescription();
        this.conversionToSqm = unitType.getConversionToSqm();
        this.isActive = unitType.getIsActive();
    }
}