package com.lyhorng.propertyservice.repository;

import com.lyhorng.propertyservice.model.BuildingFloor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingFloorRepository extends JpaRepository<BuildingFloor, Long> {
    // You can define custom queries here if needed
}
