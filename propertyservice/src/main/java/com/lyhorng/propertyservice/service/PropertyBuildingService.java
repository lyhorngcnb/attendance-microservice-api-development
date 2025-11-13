package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.PropertyBuilding;
import com.lyhorng.propertyservice.model.Category;
import com.lyhorng.propertyservice.model.Agency;
import com.lyhorng.propertyservice.model.Property;
import com.lyhorng.propertyservice.repository.PropertyBuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyBuildingService {

    @Autowired
    private PropertyBuildingRepository propertyBuildingRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private PropertyService propertyService;

    // Create PropertyBuilding
    public PropertyBuilding createPropertyBuilding(Long categoryId, Long agencyId, Long propertyId,
                                                   String buildingType, String buildingAge,
                                                   String buildingStore, String buildingUpArea) {
        // Retrieve related entities
        Category category = categoryService.findById(categoryId);
        Agency agency = agencyService.findById(agencyId);
        Property property = propertyService.findById(propertyId);

        // Create a new PropertyBuilding
        PropertyBuilding propertyBuilding = new PropertyBuilding();
        propertyBuilding.setBuildingType(buildingType);
        propertyBuilding.setBuildingAge(buildingAge);
        propertyBuilding.setBuildingStore(buildingStore);
        propertyBuilding.setBuildingUpArea(buildingUpArea);

        // Set relationships
        propertyBuilding.setCategory(category);
        propertyBuilding.setAgency(agency);
        propertyBuilding.setProperty(property);

        // Save and return the created property building
        return propertyBuildingRepository.save(propertyBuilding);
    }

    // Update PropertyBuilding
    public PropertyBuilding updatePropertyBuilding(Long id, Long categoryId, Long agencyId, Long propertyId,
                                                   String buildingType, String buildingAge,
                                                   String buildingStore, String buildingUpArea) {
        // Find existing PropertyBuilding
        PropertyBuilding existingBuilding = propertyBuildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyBuilding not found"));

        // Retrieve related entities
        Category category = categoryService.findById(categoryId);
        Agency agency = agencyService.findById(agencyId);
        Property property = propertyService.findById(propertyId);

        // Update fields
        existingBuilding.setBuildingType(buildingType);
        existingBuilding.setBuildingAge(buildingAge);
        existingBuilding.setBuildingStore(buildingStore);
        existingBuilding.setBuildingUpArea(buildingUpArea);
        existingBuilding.setCategory(category);
        existingBuilding.setAgency(agency);
        existingBuilding.setProperty(property);

        // Save and return the updated property building
        return propertyBuildingRepository.save(existingBuilding);
    }

    // Get all PropertyBuildings
    public List<PropertyBuilding> getAllPropertyBuildings() {
        return propertyBuildingRepository.findAll();
    }

    // View PropertyBuilding by ID
    public PropertyBuilding getPropertyBuilding(Long id) {
        return propertyBuildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyBuilding not found"));
    }

    // Delete PropertyBuilding by ID
    public void deletePropertyBuilding(Long id) {
        PropertyBuilding existingBuilding = propertyBuildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyBuilding not found"));
        propertyBuildingRepository.delete(existingBuilding);
    }
}
