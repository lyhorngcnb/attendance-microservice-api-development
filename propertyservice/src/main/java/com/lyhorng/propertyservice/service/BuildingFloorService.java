package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.BuildingFloor;
import com.lyhorng.propertyservice.model.PropertyBuilding;
import com.lyhorng.propertyservice.repository.BuildingFloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingFloorService {

    @Autowired
    private BuildingFloorRepository buildingFloorRepository;

    @Autowired
    private PropertyBuildingService propertyBuildingService;

    // Create BuildingFloor
    public BuildingFloor createBuildingFloor(Long propertyBuildingId, String width, String length, Integer floorType, Double approxValue) {
        // Retrieve the PropertyBuilding
        PropertyBuilding propertyBuilding = propertyBuildingService.getPropertyBuilding(propertyBuildingId);

        // Create a new BuildingFloor
        BuildingFloor buildingFloor = new BuildingFloor();
        buildingFloor.setWidth(width);
        buildingFloor.setLength(length);
        buildingFloor.setFloorType(floorType);
        buildingFloor.setApproxValue(approxValue); // Set the ApproxValue

        // Set the relationship with PropertyBuilding
        buildingFloor.setPropertyBuilding(propertyBuilding);

        // Save and return the created BuildingFloor
        return buildingFloorRepository.save(buildingFloor);
    }

    // Update BuildingFloor
    public BuildingFloor updateBuildingFloor(Long id, Long propertyBuildingId, String width, String length, Integer floorType, Double approxValue) {
        // Retrieve the existing BuildingFloor
        BuildingFloor existingFloor = buildingFloorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BuildingFloor not found"));

        // Retrieve the PropertyBuilding
        PropertyBuilding propertyBuilding = propertyBuildingService.getPropertyBuilding(propertyBuildingId);

        // Update fields
        existingFloor.setWidth(width);
        existingFloor.setLength(length);
        existingFloor.setFloorType(floorType);
        existingFloor.setApproxValue(approxValue); // Update ApproxValue
        existingFloor.setPropertyBuilding(propertyBuilding);

        // Save and return the updated BuildingFloor
        return buildingFloorRepository.save(existingFloor);
    }

    // Get all BuildingFloors
    public List<BuildingFloor> getAllBuildingFloors() {
        return buildingFloorRepository.findAll();
    }

    // Get BuildingFloor by ID
    public BuildingFloor getBuildingFloor(Long id) {
        return buildingFloorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BuildingFloor not found"));
    }

    // Delete BuildingFloor by ID
    public void deleteBuildingFloor(Long id) {
        BuildingFloor existingFloor = buildingFloorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BuildingFloor not found"));
        buildingFloorRepository.delete(existingFloor);
    }
}
