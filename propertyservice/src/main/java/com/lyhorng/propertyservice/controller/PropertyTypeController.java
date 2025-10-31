package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyType;
import com.lyhorng.propertyservice.service.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/propertytypes")
public class PropertyTypeController {

    @Autowired
    private PropertyTypeService propertyTypeService;

    // Route for listing all property  types
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyType>>> listAll() {
        List<PropertyType> propertyTypes = propertyTypeService.getAllPropertyTypes();
        return ResponseEntity.ok(ApiResponse.success("Property Types fetched successfully.", propertyTypes));
    }

    // Route for creating a new property  type
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyType>> create(
            @RequestParam("type") String Type,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyType propertyType = propertyTypeService.createPropertyType(Type, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Property  Type created successfully.", propertyType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to create Property  Type: " + e.getMessage(), null));
        }
    }

    // Route for updating an existing property  type
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyType>> update(
            @RequestParam("id") Long id,
            @RequestParam("type") String Type,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyType propertyType = propertyTypeService.updatePropertyType(id, Type, file);
            return ResponseEntity.ok(ApiResponse.success("Property  Type updated successfully.", propertyType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to update Property  Type: " + e.getMessage(), null));
        }
    }

    // Route for deleting a property  type
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyTypeService.deletePropertyType(id);
            return ResponseEntity.ok(ApiResponse.success("Property  Type deleted successfully.", "Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to delete Property  Type: " + e.getMessage(), null));
        }
    }

    // Route for viewing (getting details of) a property  type
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyType>> view(@RequestParam("id") Long id) {
        try {
            PropertyType propertyType = propertyTypeService.getPropertyTypeById(id);
            return ResponseEntity.ok(ApiResponse.success("Property  Type details fetched successfully.", propertyType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Property  Type not found: " + e.getMessage(), null));
        }
    }
}
