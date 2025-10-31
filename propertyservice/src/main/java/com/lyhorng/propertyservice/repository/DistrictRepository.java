package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.District;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    // Custom queries can be added here if needed
    List<District> findByProvinceId(Long provinceId);
}
