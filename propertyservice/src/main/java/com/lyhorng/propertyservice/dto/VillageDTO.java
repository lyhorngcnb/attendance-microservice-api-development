package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VillageDTO {
    private Long id;
    private String type;
    private String code;
    private String khmerName;
    private String name;
    private Long communeId;
    private Long districtId;
    private Long provinceId;
    
    public VillageDTO(Village village) {
        this.id = village.getId();
        this.type = village.getType();
        this.code = village.getCode();
        this.khmerName = village.getKhmerName();
        this.name = village.getName();
        this.communeId = village.getCommuneId();
        this.districtId = village.getDistrictId();
        this.provinceId = village.getProvinceId();
    }
}