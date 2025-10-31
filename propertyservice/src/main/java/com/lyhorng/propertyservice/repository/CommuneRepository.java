package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.Commune;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {
    // Custom queries can be added here if needed
    List<Commune> findByDistrictId(Long districtId);
    
    // Optional: Find communes by province ID directly
    List<Commune> findByProvinceId(Long provinceId);
}
