package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyBuilding;
import com.lyhorng.propertyservice.service.PropertyBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property-building")
public class PropertyBuildingController {

    @Autowired
    private PropertyBuildingService propertyBuildingService;

    // Create PropertyBuilding
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyBuilding>> create(@RequestParam("buildingType") String buildingType,
                                                                @RequestParam("buildingAge") String buildingAge,
                                                                @RequestParam(value = "buildingStore", required = false) String buildingStore,
                                                                @RequestParam(value = "buildingUpArea", required = false) String buildingUpArea,
                                                                @RequestParam("categoryId") Long categoryId,
                                                                @RequestParam("agencyId") Long agencyId,
                                                                @RequestParam("propertyId") Long propertyId) {
        try {
            PropertyBuilding propertyBuilding = propertyBuildingService.createPropertyBuilding(
                    categoryId, agencyId, propertyId, buildingType, buildingAge, buildingStore, buildingUpArea);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Property building created successfully.", propertyBuilding));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create property building: " + e.getMessage(), null));
        }
    }

    // Update PropertyBuilding
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyBuilding>> update(@RequestParam("id") Long id,
                                                                @RequestParam("buildingType") String buildingType,
                                                                @RequestParam("buildingAge") String buildingAge,
                                                                @RequestParam(value = "buildingStore", required = false) String buildingStore,
                                                                @RequestParam(value = "buildingUpArea", required = false) String buildingUpArea,
                                                                @RequestParam("categoryId") Long categoryId,
                                                                @RequestParam("agencyId") Long agencyId,
                                                                @RequestParam("propertyId") Long propertyId) {
        try {
            PropertyBuilding updatedBuilding = propertyBuildingService.updatePropertyBuilding(
                    id, categoryId, agencyId, propertyId, buildingType, buildingAge, buildingStore, buildingUpArea);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Property building updated successfully.", updatedBuilding));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update property building: " + e.getMessage(), null));
        }
    }

    // View PropertyBuilding by ID
    @GetMapping("/view/{id}")
    public ResponseEntity<ApiResponse<PropertyBuilding>> view(@PathVariable("id") Long id) {
        try {
            PropertyBuilding propertyBuilding = propertyBuildingService.getPropertyBuilding(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Property building fetched successfully.", propertyBuilding));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property building not found: " + e.getMessage(), null));
        }
    }

    // Get all PropertyBuildings
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyBuilding>>> list() {
        List<PropertyBuilding> propertyBuildings = propertyBuildingService.getAllPropertyBuildings();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Property buildings fetched successfully.", propertyBuildings));
    }

    // Delete PropertyBuilding by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long id) {
        try {
            propertyBuildingService.deletePropertyBuilding(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Property building deleted successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property building: " + e.getMessage(), null));
        }
    }
}
