package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.PropertyLand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyLandRepository extends JpaRepository<PropertyLand, Long> {
    
    // Find all lands for a specific property
    List<PropertyLand> findByPropertyId(Long propertyId);
    
    // Find land by property ID (if only one land per property)
    Optional<PropertyLand> findFirstByPropertyId(Long propertyId);
    
    // Find by lot number
    Optional<PropertyLand> findByLotNumber(String lotNumber);
    
    // Find by property unit type
    List<PropertyLand> findByPropertyUnitTypeId(Long propertyUnitTypeId);
    
    // Delete all lands for a property
    void deleteByPropertyId(Long propertyId);
    
    // Check if property has land records
    boolean existsByPropertyId(Long propertyId);
}
