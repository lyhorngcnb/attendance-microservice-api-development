package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.BuildingFloor;
import com.lyhorng.propertyservice.service.BuildingFloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building-floor")
public class BuildingFloorController {

    @Autowired
    private BuildingFloorService buildingFloorService;

    // Create BuildingFloor
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BuildingFloor>> create(@RequestParam("propertyBuildingId") Long propertyBuildingId,
                                                              @RequestParam("width") String width,
                                                              @RequestParam("length") String length,
                                                              @RequestParam("floorType") Integer floorType,
                                                              @RequestParam("approxValue") Double approxValue) {
        try {
            BuildingFloor buildingFloor = buildingFloorService.createBuildingFloor(propertyBuildingId, width, length, floorType, approxValue);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Building floor created successfully.", buildingFloor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create building floor: " + e.getMessage(), null));
        }
    }

    // Update BuildingFloor
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<BuildingFloor>> update(@RequestParam("id") Long id,
                                                              @RequestParam("propertyBuildingId") Long propertyBuildingId,
                                                              @RequestParam("width") String width,
                                                              @RequestParam("length") String length,
                                                              @RequestParam("floorType") Integer floorType,
                                                              @RequestParam("approxValue") Double approxValue) {
        try {
            BuildingFloor updatedFloor = buildingFloorService.updateBuildingFloor(id, propertyBuildingId, width, length, floorType, approxValue);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Building floor updated successfully.", updatedFloor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update building floor: " + e.getMessage(), null));
        }
    }

    // View BuildingFloor by ID
    @GetMapping("/view/{id}")
    public ResponseEntity<ApiResponse<BuildingFloor>> view(@PathVariable("id") Long id) {
        try {
            BuildingFloor buildingFloor = buildingFloorService.getBuildingFloor(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Building floor fetched successfully.", buildingFloor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Building floor not found: " + e.getMessage(), null));
        }
    }

    // Get all BuildingFloors
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BuildingFloor>>> list() {
        List<BuildingFloor> buildingFloors = buildingFloorService.getAllBuildingFloors();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Building floors fetched successfully.", buildingFloors));
    }

    // Delete BuildingFloor by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long id) {
        try {
            buildingFloorService.deleteBuildingFloor(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("Building floor deleted successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete building floor: " + e.getMessage(), null));
        }
    }
}
