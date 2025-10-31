package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PropertyDTO {
    private Long id;
    private Integer branchRequestId;
    private String oldPropertyId;
    private String propertyCode;
    private String propertyVersion;
    private Boolean isOwner;
    private String titleNumber;
    private String remark;
    private String filePath;
    
    // Nested objects for related entities
    private RelatedEntityDTO titleType;
    private RelatedEntityDTO propertyType;
    private RelatedEntityDTO propertyInfo;
    private RelatedEntityDTO measureInfo;
    private LocationDTO province;
    private LocationDTO district;
    private LocationDTO commune;
    private LocationDTO village;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PropertyDTO(Property property) {
        this.id = property.getId();
        this.branchRequestId = property.getBranchRequestId();
        this.oldPropertyId = property.getOldPropertyId();
        this.propertyCode = property.getPropertyCode();
        this.propertyVersion = property.getPropertyVersion();
        this.isOwner = property.getIsOwner();
        this.titleNumber = property.getTitleNumber();
        this.remark = property.getRemark();
        this.filePath = property.getFilePath();
        this.createdAt = property.getCreatedAt();
        this.updatedAt = property.getUpdatedAt();
        
        // Map related entities to nested DTOs
        this.titleType = property.getPropertyTitleType() != null 
            ? new RelatedEntityDTO(property.getPropertyTitleType()) 
            : null;
            
        this.propertyType = property.getPropertyType() != null 
            ? new RelatedEntityDTO(property.getPropertyType()) 
            : null;
            
        this.propertyInfo = property.getPropertyInfo() != null 
            ? new RelatedEntityDTO(property.getPropertyInfo()) 
            : null;
            
        this.measureInfo = property.getMeasureInfo() != null 
            ? new RelatedEntityDTO(property.getMeasureInfo()) 
            : null;
            
        this.province = property.getProvince() != null 
            ? new LocationDTO(property.getProvince()) 
            : null;
            
        this.district = property.getDistrict() != null 
            ? new LocationDTO(property.getDistrict()) 
            : null;
            
        this.commune = property.getCommune() != null 
            ? new LocationDTO(property.getCommune()) 
            : null;
            
        this.village = property.getVillage() != null 
            ? new LocationDTO(property.getVillage()) 
            : null;
    }

    // Inner class for generic related entities (PropertyType, PropertyInfo, etc.)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedEntityDTO {
        private Long id;
        private String name; // Adjust field name based on your actual entity
        
        // Constructor that works with any entity that has getId()
        public RelatedEntityDTO(Object entity) {
            try {
                this.id = (Long) entity.getClass().getMethod("getId").invoke(entity);
                // Try common name field patterns
                try {
                    this.name = (String) entity.getClass().getMethod("getName").invoke(entity);
                } catch (NoSuchMethodException e1) {
                    try {
                        this.name = (String) entity.getClass().getMethod("getTitle").invoke(entity);
                    } catch (NoSuchMethodException e2) {
                        try {
                            this.name = (String) entity.getClass().getMethod("getDescription").invoke(entity);
                        } catch (NoSuchMethodException e3) {
                            this.name = "N/A";
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error mapping entity to DTO", e);
            }
        }
    }

    // Inner class for location entities (Province, District, Commune, Village)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDTO {
        private Long id;
        private String name;
        private String nameKh; // Khmer name if available
        
        public LocationDTO(Object entity) {
            try {
                this.id = (Long) entity.getClass().getMethod("getId").invoke(entity);
                try {
                    this.name = (String) entity.getClass().getMethod("getName").invoke(entity);
                } catch (NoSuchMethodException e) {
                    this.name = "N/A";
                }
                try {
                    this.nameKh = (String) entity.getClass().getMethod("getNameKh").invoke(entity);
                } catch (NoSuchMethodException e) {
                    this.nameKh = null;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error mapping location entity to DTO", e);
            }
        }
        
        // Specific constructors for each location type
        public LocationDTO(Province province) {
            this.id = province.getId();
            // Use the actual field name from your Province entity
            this.name = getFieldValue(province, "name", "title", "provinceName");
            this.nameKh = getFieldValue(province, "nameKh", "khmerName");
        }
        
        public LocationDTO(District district) {
            this.id = district.getId();
            this.name = getFieldValue(district, "name", "title", "districtName");
            this.nameKh = getFieldValue(district, "nameKh", "khmerName");
        }
        
        public LocationDTO(Commune commune) {
            this.id = commune.getId();
            this.name = getFieldValue(commune, "name", "title", "communeName");
            this.nameKh = getFieldValue(commune, "nameKh", "khmerName");
        }
        
        public LocationDTO(Village village) {
            this.id = village.getId();
            this.name = getFieldValue(village, "name", "title", "villageName");
            this.nameKh = getFieldValue(village, "nameKh", "khmerName");
        }
        
        private String getFieldValue(Object obj, String... possibleFieldNames) {
            for (String fieldName : possibleFieldNames) {
                try {
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    return (String) obj.getClass().getMethod(methodName).invoke(obj);
                } catch (Exception e) {
                    // Try next field name
                }
            }
            return null;
        }
    }
}