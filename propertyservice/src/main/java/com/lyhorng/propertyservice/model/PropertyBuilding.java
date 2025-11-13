package com.lyhorng.propertyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "property_building")
@NoArgsConstructor
@AllArgsConstructor
public class PropertyBuilding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_type", nullable = false, length = 50)
    private String buildingType;

    @Column(name = "building_age", nullable = false, length = 50)
    private String buildingAge;

    @Column(name =  "building_store", nullable = true, length = 50)
    private String buildingStore;

    @Column(name = "building_up_area", nullable = true, length = 50)
    private String buildingUpArea;

    // Relationship with Category
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category; // assuming Category is another entity

    // Relationship with Agency
    @ManyToOne
    @JoinColumn(name = "agency_id", referencedColumnName = "id", nullable = false)
    private Agency agency; // assuming Agency is another entity

    // Relationship with Property
    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property property; // assuming Property is another entity
}
