package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyTittleType;
import com.lyhorng.propertyservice.service.PropertyTittleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/propertytitletypes")
public class PropertyTittleTypeController {

    @Autowired
    private PropertyTittleTypeService propertyTittleTypeService;

    // Route for listing all property title types
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyTittleType>>> listAll() {
        List<PropertyTittleType> propertyTittleTypes = propertyTittleTypeService.getAllPropertyTitleTypes();
        return ResponseEntity.ok(ApiResponse.success("Property Title Types fetched successfully.", propertyTittleTypes));
    }

    // Route for creating a new property title type
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyTittleType>> create(
            @RequestParam("titleType") String titleType,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyTittleType propertyTittleType = propertyTittleTypeService.createPropertyTittleType(titleType, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Property Title Type created successfully.", propertyTittleType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to create Property Title Type: " + e.getMessage(), null));
        }
    }

    // Route for updating an existing property title type
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyTittleType>> update(
            @RequestParam("id") Long id,
            @RequestParam("titleType") String titleType,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyTittleType propertyTittleType = propertyTittleTypeService.updatePropertyTittleType(id, titleType, file);
            return ResponseEntity.ok(ApiResponse.success("Property Title Type updated successfully.", propertyTittleType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to update Property Title Type: " + e.getMessage(), null));
        }
    }

    // Route for deleting a property title type
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyTittleTypeService.deletePropertyTittleType(id);
            return ResponseEntity.ok(ApiResponse.success("Property Title Type deleted successfully.", "Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to delete Property Title Type: " + e.getMessage(), null));
        }
    }

    // Route for viewing (getting details of) a property title type
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyTittleType>> view(@RequestParam("id") Long id) {
        try {
            PropertyTittleType propertyTittleType = propertyTittleTypeService.getPropertyTittleTypeById(id);
            return ResponseEntity.ok(ApiResponse.success("Property Title Type details fetched successfully.", propertyTittleType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Property Title Type not found: " + e.getMessage(), null));
        }
    }
}
