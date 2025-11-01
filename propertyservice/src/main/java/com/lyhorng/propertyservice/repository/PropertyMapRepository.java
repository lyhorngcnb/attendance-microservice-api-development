package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.PropertyMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyMapRepository extends JpaRepository<PropertyMap, Long> {
    
    // Find all maps for a specific property
    List<PropertyMap> findByPropertyId(Long propertyId);
    
    // Find map by property ID (if only one map per property)
    Optional<PropertyMap> findFirstByPropertyId(Long propertyId);
    
    // Delete all maps for a property
    void deleteByPropertyId(Long propertyId);
    
    // Check if property has maps
    boolean existsByPropertyId(Long propertyId);
    
    // Find maps within a bounding box (for map view)
    List<PropertyMap> findByLatitudeBetweenAndLongitudeBetween(
        Double minLat, Double maxLat, Double minLng, Double maxLng
    );
}