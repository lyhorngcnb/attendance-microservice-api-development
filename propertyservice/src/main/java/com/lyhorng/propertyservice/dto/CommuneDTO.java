package com.lyhorng.propertyservice.dto;

import com.lyhorng.propertyservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommuneDTO {
    private Long id;
    private String type;
    private String code;
    private String khmerName;
    private String name;
    private Long districtId;
    private Long provinceId;
    
    public CommuneDTO(Commune commune) {
        this.id = commune.getId();
        this.type = commune.getType();
        this.code = commune.getCode();
        this.khmerName = commune.getKhmerName();
        this.name = commune.getName();
        this.districtId = commune.getDistrictId();
        this.provinceId = commune.getProvinceId();
    }
}