package com.lyhorng.propertyservice.dto;
import com.lyhorng.propertyservice.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DistrictDTO {
    private Long id;
    private String type;
    private String code;
    private String khmerName;
    private String name;
    private Long provinceId;
    
    public DistrictDTO(District district) {
        this.id = district.getId();
        this.type = district.getType();
        this.code = district.getCode();
        this.khmerName = district.getKhmerName();
        this.name = district.getName();
        this.provinceId = district.getProvinceId();
    }
}
