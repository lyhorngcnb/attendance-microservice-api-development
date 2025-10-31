package com.lyhorng.propertyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "branch_request_id")
    private Integer branchRequestId;
    
    @Column(name = "old_property_id", length = 50)
    private String oldPropertyId;
    
    @Column(name = "property_code", length = 50, unique = true)
    private String propertyCode;
    
    @Column(name = "property_version", length = 20)
    private String propertyVersion;
    
    @Column(name = "is_owner")
    private Boolean isOwner = false;
    
    @Column(name = "title_number", length = 100)
    private String titleNumber;
    
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
    
    // ================== RELATIONSHIPS ==================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_title_type_id")
    private PropertyTittleType propertyTitleType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_type_id")
    private PropertyType propertyType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_info_id")
    private PropertyInfo propertyInfo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "measure_info_id")
    private PropertyMeasureInfo measureInfo;
    
    // ================== LOCATION RELATIONSHIPS ==================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commune_id")
    private Commune commune;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "village_id")
    private Village village;
    
    // ================== AUDIT FIELDS ==================
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "file_path")
    private String filePath;
}