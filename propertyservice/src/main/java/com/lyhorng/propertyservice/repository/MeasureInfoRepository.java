package com.lyhorng.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lyhorng.propertyservice.model.PropertyMeasureInfo;

@Repository
public interface MeasureInfoRepository extends JpaRepository<PropertyMeasureInfo, Long> {
    // Additional query methods can be defined here if necessary
}
