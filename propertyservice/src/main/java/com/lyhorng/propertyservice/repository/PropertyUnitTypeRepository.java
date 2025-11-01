// ===============================================================

package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.PropertyUnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyUnitTypeRepository extends JpaRepository<PropertyUnitType, Long> {
    
    // Find by code
    Optional<PropertyUnitType> findByCode(String code);
    
    // Find active unit types
    List<PropertyUnitType> findByIsActiveTrue();
    
    // Find by name
    Optional<PropertyUnitType> findByName(String name);
}