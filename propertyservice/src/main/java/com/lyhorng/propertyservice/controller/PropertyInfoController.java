package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyInfo;
import com.lyhorng.propertyservice.service.PropertyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propertyinfo")
public class PropertyInfoController {

    @Autowired
    private PropertyInfoService propertyInfoService;

    // Route for listing all Property Info
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyInfo>>> listAll() {
        List<PropertyInfo> propertyInfoList = propertyInfoService.getAllPropertyInfo();
        return ResponseEntity.ok(ApiResponse.success("Property info fetched successfully.", propertyInfoList));
    }

    // Route for creating a new Property Info
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyInfo>> create(@RequestParam("propertyInfo") String propertyInfo) {
        try {
            PropertyInfo newPropertyInfo = propertyInfoService.createPropertyInfo(propertyInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Property info created successfully.", newPropertyInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to create property info: " + e.getMessage(), null));
        }
    }

    // Route for updating an existing Property Info
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyInfo>> update(
            @RequestParam("id") Long id,
            @RequestParam("propertyInfo") String propertyInfo) {
        try {
            PropertyInfo updatedPropertyInfo = propertyInfoService.updatePropertyInfo(id, propertyInfo);
            return ResponseEntity.ok(ApiResponse.success("Property info updated successfully.", updatedPropertyInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to update property info: " + e.getMessage(), null));
        }
    }

    // Route for deleting a Property Info
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyInfoService.deletePropertyInfo(id);
            return ResponseEntity.ok(ApiResponse.success("Property info deleted successfully.", "Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to delete property info: " + e.getMessage(), null));
        }
    }

    // Route for viewing (getting details of) a Property Info
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyInfo>> view(@RequestParam("id") Long id) {
        try {
            PropertyInfo propertyInfo = propertyInfoService.getPropertyInfo(id);
            return ResponseEntity.ok(ApiResponse.success("Property info fetched successfully.", propertyInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Property info not found: " + e.getMessage(), null));
        }
    }
}
