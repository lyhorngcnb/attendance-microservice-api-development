package com.lyhorng.propertyservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "building_floor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "width")
    private String width;

    @Column(name = "length")
    private String length;

    @Column(name = "floor_type")
    private Integer floorType;

    // Many-to-One relationship with PropertyBuilding
    @ManyToOne
    @JoinColumn(name = "property_building_id", referencedColumnName = "id", nullable = false)
    private PropertyBuilding propertyBuilding;

    // New field for ApproxValue
    @Column(name = "approx_value")
    private Double approxValue; // Assuming ApproxValue is a Double (you can change this if needed)
}
