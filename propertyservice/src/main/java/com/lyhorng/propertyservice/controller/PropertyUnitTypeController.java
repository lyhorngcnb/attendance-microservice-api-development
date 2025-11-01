package com.lyhorng.propertyservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyUnitType;
import com.lyhorng.propertyservice.service.PropertyUnitTypeService;

@RestController
@RequestMapping("/api/property-unittypes")
public class PropertyUnitTypeController {

    @Autowired
    private PropertyUnitTypeService propertyUnitTypeService;

    // ===================== LIST =====================
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyUnitType>>> listAll() {
        try {
            List<PropertyUnitType> propertyUnitTypes = propertyUnitTypeService.getAllPropertyUnitType();
            return ResponseEntity.ok(ApiResponse.success("Property Unit Types fetched successfully", propertyUnitTypes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve Property Unit Types: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyUnitType>> create(@RequestParam("unitType") String unitType) {
        try {
            PropertyUnitType propertyUnitType = propertyUnitTypeService.createPropertyUnitType(unitType);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Property Unit Type created successfully", propertyUnitType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create Property Unit Type: " + e.getMessage(), "CREATE_ERROR"));
        }
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyUnitType>> update(
            @RequestParam("id") Long id,
            @RequestParam("unitType") String unitType) {
        try {
            PropertyUnitType updatedPropertyUnitType = propertyUnitTypeService.updatePerPropertyUnitType(id, unitType);
            return ResponseEntity.ok(ApiResponse.success("Property Unit Type updated successfully", updatedPropertyUnitType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update Property Unit Type: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // ===================== DELETE =====================
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyUnitTypeService.deletePropertyUnitType(id);
            return ResponseEntity.ok(ApiResponse.success("Property Unit Type deleted successfully", "Deleted"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete Property Unit Type: " + e.getMessage(), "DELETE_ERROR"));
        }
    }

    // ===================== VIEW =====================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyUnitType>> view(@RequestParam("id") Long id) {
        try {
            PropertyUnitType propertyUnitType = propertyUnitTypeService.getPropertyUnitType(id);
            return ResponseEntity.ok(ApiResponse.success("Property Unit Type retrieved successfully", propertyUnitType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property Unit Type not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }
}
