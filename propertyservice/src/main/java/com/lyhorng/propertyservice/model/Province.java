// Province.java (No relationships, keep as is)
package com.lyhorng.propertyservice.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provinces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {
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
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}