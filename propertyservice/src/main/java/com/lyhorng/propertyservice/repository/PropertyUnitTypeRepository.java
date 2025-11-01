package com.lyhorng.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lyhorng.propertyservice.model.PropertyUnitType;

public interface PropertyUnitTypeRepository extends JpaRepository<PropertyUnitType, Long> {
    
}
