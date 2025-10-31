package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// ===================== PROVINCE DTO =====================
@Data
@NoArgsConstructor
public class ProvinceDTO {
    private Long id;
    private String type;
    private String code;
    private String khmerName;
    private String name;
    
    public ProvinceDTO(Province province) {
        this.id = province.getId();
        this.type = province.getType();
        this.code = province.getCode();
        this.khmerName = province.getKhmerName();
        this.name = province.getName();
    }
}