package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    // Custom queries can be added here if needed
}
