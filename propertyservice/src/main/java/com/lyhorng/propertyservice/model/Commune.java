// Commune.java (LAZY fetch, no nested objects in JSON)
package com.lyhorng.propertyservice.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "communes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "type", length = 255)
    private String type;
    
    @Column(name = "code", length = 255)
    private String code;
    
    @Column(name = "khmer_name", length = 255)
    private String khmerName;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    @JsonIgnore
    private District district;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    @JsonIgnore
    private Province province;
    
    // Helper columns to expose IDs without loading relationships
    @Column(name = "district_id", insertable = false, updatable = false)
    private Long districtId;
    
    @Column(name = "province_id", insertable = false, updatable = false)
    private Long provinceId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
