package com.lyhorng.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lyhorng.propertyservice.model.PropertyType;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {
    // Additional query methods can be defined here if necessary
}
