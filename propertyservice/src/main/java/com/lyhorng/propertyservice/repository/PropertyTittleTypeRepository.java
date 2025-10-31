package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.PropertyTittleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyTittleTypeRepository extends JpaRepository<PropertyTittleType, Long> {
    // Additional query methods can be defined here if necessary
}
