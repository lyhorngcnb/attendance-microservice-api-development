package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.PropertyBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyBuildingRepository extends JpaRepository<PropertyBuilding, Long> {
    // Custom queries can be added here if necessary
}
