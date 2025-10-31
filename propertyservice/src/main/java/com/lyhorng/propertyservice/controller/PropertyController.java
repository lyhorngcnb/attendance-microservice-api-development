package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.dto.PropertyDTO;
import com.lyhorng.propertyservice.model.Property;
import com.lyhorng.propertyservice.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    // ===================== LIST =====================
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyDTO>>> listAll() {
        try {
            List<Property> properties = propertyService.getAllProperties();
            List<PropertyDTO> propertyDTOs = properties.stream()
                    .map(PropertyDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Properties retrieved successfully", propertyDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve properties: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Property>> create(
            @RequestParam(value = "branchRequestId", required = false) Integer branchRequestId,
            @RequestParam(value = "oldPropertyId", required = false) String oldPropertyId,
            @RequestParam("propertyCode") String propertyCode,
            @RequestParam(value = "propertyVersion", required = false) String propertyVersion,
            @RequestParam(value = "isOwner", required = false) Boolean isOwner,
            @RequestParam(value = "titleNumber", required = false) String titleNumber,
            @RequestParam(value = "remark", required = false) String remark,
            @RequestParam(value = "titleTypeId", required = false) Long titleTypeId,
            @RequestParam(value = "propertyTypeId", required = false) Long propertyTypeId,
            @RequestParam(value = "propertyInfoId", required = false) Long propertyInfoId,
            @RequestParam(value = "measureInfoId", required = false) Long measureInfoId,
            @RequestParam(value = "provinceId", required = false) Long provinceId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            @RequestParam(value = "communeId", required = false) Long communeId,
            @RequestParam(value = "villageId", required = false) Long villageId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Property created = propertyService.createProperty(branchRequestId, oldPropertyId, propertyCode,
                    propertyVersion, isOwner, titleNumber, remark, titleTypeId, propertyTypeId,
                    propertyInfoId, measureInfoId, provinceId, districtId, communeId, villageId, file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Property created successfully", created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Failed to create property: " + e.getMessage(), "CREATE_ERROR"));
        }
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Property>> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "branchRequestId", required = false) Integer branchRequestId,
            @RequestParam(value = "oldPropertyId", required = false) String oldPropertyId,
            @RequestParam("propertyCode") String propertyCode,
            @RequestParam(value = "propertyVersion", required = false) String propertyVersion,
            @RequestParam(value = "isOwner", required = false) Boolean isOwner,
            @RequestParam(value = "titleNumber", required = false) String titleNumber,
            @RequestParam(value = "remark", required = false) String remark,
            @RequestParam(value = "titleTypeId", required = false) Long titleTypeId,
            @RequestParam(value = "propertyTypeId", required = false) Long propertyTypeId,
            @RequestParam(value = "propertyInfoId", required = false) Long propertyInfoId,
            @RequestParam(value = "measureInfoId", required = false) Long measureInfoId,
            @RequestParam(value = "provinceId", required = false) Long provinceId,
            @RequestParam(value = "districtId", required = false) Long districtId,
            @RequestParam(value = "communeId", required = false) Long communeId,
            @RequestParam(value = "villageId", required = false) Long villageId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Property updated = propertyService.updateProperty(id, branchRequestId, oldPropertyId, propertyCode,
                    propertyVersion, isOwner, titleNumber, remark, titleTypeId, propertyTypeId,
                    propertyInfoId, measureInfoId, provinceId, districtId, communeId, villageId, file);
            return ResponseEntity.ok(ApiResponse.success("Property updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update property: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // ===================== DELETE =====================
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok(ApiResponse.success("Property deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property: " + e.getMessage(), "DELETE_ERROR"));
        }
    }

    // ===================== VIEW =====================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<Property>> view(@RequestParam("id") Long id) {
        try {
            Property property = propertyService.getPropertyById(id);
            return ResponseEntity.ok(ApiResponse.success("Property retrieved successfully", property));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }
}
