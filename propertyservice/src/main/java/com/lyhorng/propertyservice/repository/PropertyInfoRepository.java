package com.lyhorng.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lyhorng.propertyservice.model.PropertyInfo;

public interface PropertyInfoRepository extends JpaRepository<PropertyInfo, Long>{
    
}