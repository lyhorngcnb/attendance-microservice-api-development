package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.dto.PropertyMapDTO;
import com.lyhorng.propertyservice.model.PropertyMap;
import com.lyhorng.propertyservice.service.PropertyMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/property-maps")
@CrossOrigin(origins = "*")
public class PropertyMapController {

    @Autowired
    private PropertyMapService propertyMapService;

    // ===================== LIST ALL =====================
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyMapDTO>>> listAll() {
        try {
            List<PropertyMap> maps = propertyMapService.getAllPropertyMaps();
            List<PropertyMapDTO> dtos = maps.stream()
                    .map(PropertyMapDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property maps retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property maps: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== GET BY PROPERTY ID =====================
    @GetMapping("/by-property/{propertyId}")
    public ResponseEntity<ApiResponse<List<PropertyMapDTO>>> getByPropertyId(
            @PathVariable Long propertyId) {
        try {
            List<PropertyMap> maps = propertyMapService.getMapsByPropertyId(propertyId);
            List<PropertyMapDTO> dtos = maps.stream()
                    .map(PropertyMapDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property maps retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property maps: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== VIEW BY ID =====================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyMapDTO>> view(@RequestParam("id") Long id) {
        try {
            PropertyMap propertyMap = propertyMapService.getPropertyMapById(id);
            PropertyMapDTO dto = new PropertyMapDTO(propertyMap);
            return ResponseEntity.ok(
                    ApiResponse.success("Property map retrieved successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property map not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyMapDTO>> create(
            @RequestParam(value = "mapData", required = false) String mapData,
            @RequestParam("color") String color,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("propertyId") Long propertyId,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) {
        try {
            PropertyMap created = propertyMapService.createPropertyMap(
                    mapData, color, latitude, longitude, propertyId, attachment);
            PropertyMapDTO dto = new PropertyMapDTO(created);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Property map created successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create property map: " + e.getMessage(), "CREATE_ERROR"));
        }
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyMapDTO>> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "mapData", required = false) String mapData,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "propertyId", required = false) Long propertyId,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) {
        try {
            PropertyMap updated = propertyMapService.updatePropertyMap(
                    id, mapData, color, latitude, longitude, propertyId, attachment);
            PropertyMapDTO dto = new PropertyMapDTO(updated);
            return ResponseEntity.ok(
                    ApiResponse.success("Property map updated successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update property map: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // ===================== DELETE =====================
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyMapService.deletePropertyMap(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Property map deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property map: " + e.getMessage(), "DELETE_ERROR"));
        }
    }

    // ===================== DELETE BY PROPERTY ID =====================
    @PostMapping("/delete-by-property/{propertyId}")
    public ResponseEntity<ApiResponse<String>> deleteByPropertyId(@PathVariable Long propertyId) {
        try {
            propertyMapService.deleteMapsByPropertyId(propertyId);
            return ResponseEntity.ok(
                    ApiResponse.success("Property maps deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property maps: " + e.getMessage(), "DELETE_ERROR"));
        }
    }

    // ===================== GET MAPS IN BOUNDING BOX =====================
    @GetMapping("/in-bounds")
    public ResponseEntity<ApiResponse<List<PropertyMapDTO>>> getMapsInBounds(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLng,
            @RequestParam Double maxLng) {
        try {
            List<PropertyMap> maps = propertyMapService.getMapsInBoundingBox(
                    minLat, maxLat, minLng, maxLng);
            List<PropertyMapDTO> dtos = maps.stream()
                    .map(PropertyMapDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property maps retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property maps: " + e.getMessage(), "BOUNDS_ERROR"));
        }
    }
}