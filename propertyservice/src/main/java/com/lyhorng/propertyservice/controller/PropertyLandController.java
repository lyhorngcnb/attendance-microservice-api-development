package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.dto.PropertyLandDTO;
import com.lyhorng.propertyservice.model.PropertyLand;
import com.lyhorng.propertyservice.service.PropertyLandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/property-lands")
@CrossOrigin(origins = "*")
public class PropertyLandController {

    @Autowired
    private PropertyLandService propertyLandService;

    // ===================== LIST ALL =====================
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyLandDTO>>> listAll() {
        try {
            List<PropertyLand> lands = propertyLandService.getAllPropertyLands();
            List<PropertyLandDTO> dtos = lands.stream()
                    .map(PropertyLandDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property lands retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property lands: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== GET BY PROPERTY ID =====================
    @GetMapping("/by-property/{propertyId}")
    public ResponseEntity<ApiResponse<List<PropertyLandDTO>>> getByPropertyId(
            @PathVariable Long propertyId) {
        try {
            List<PropertyLand> lands = propertyLandService.getLandsByPropertyId(propertyId);
            List<PropertyLandDTO> dtos = lands.stream()
                    .map(PropertyLandDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property lands retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve property lands: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== VIEW BY ID =====================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyLandDTO>> view(@RequestParam("id") Long id) {
        try {
            PropertyLand propertyLand = propertyLandService.getPropertyLandById(id);
            PropertyLandDTO dto = new PropertyLandDTO(propertyLand);
            return ResponseEntity.ok(
                    ApiResponse.success("Property land retrieved successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property land not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }

    // ===================== GET BY LOT NUMBER =====================
    @GetMapping("/by-lot/{lotNumber}")
    public ResponseEntity<ApiResponse<PropertyLandDTO>> getByLotNumber(
            @PathVariable String lotNumber) {
        try {
            PropertyLand propertyLand = propertyLandService.getByLotNumber(lotNumber);
            PropertyLandDTO dto = new PropertyLandDTO(propertyLand);
            return ResponseEntity.ok(
                    ApiResponse.success("Property land retrieved successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Property land not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyLandDTO>> create(
            @RequestParam(value = "landSize", required = false) BigDecimal landSize,
            @RequestParam(value = "front", required = false) BigDecimal front,
            @RequestParam(value = "back", required = false) BigDecimal back,
            @RequestParam(value = "length", required = false) BigDecimal length,
            @RequestParam(value = "width", required = false) BigDecimal width,
            @RequestParam(value = "dimensionLand", required = false) String dimensionLand,
            @RequestParam(value = "lotNumber", required = false) String lotNumber,
            @RequestParam(value = "propertyUnitTypeId", required = false) Long propertyUnitTypeId,
            @RequestParam("propertyId") Long propertyId) {
        try {
            PropertyLand created = propertyLandService.createPropertyLand(
                    landSize, front, back, length, width, dimensionLand, lotNumber,
                    propertyUnitTypeId, propertyId);
            PropertyLandDTO dto = new PropertyLandDTO(created);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Property land created successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create property land: " + e.getMessage(), "CREATE_ERROR"));
        }
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyLandDTO>> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "landSize", required = false) BigDecimal landSize,
            @RequestParam(value = "front", required = false) BigDecimal front,
            @RequestParam(value = "back", required = false) BigDecimal back,
            @RequestParam(value = "length", required = false) BigDecimal length,
            @RequestParam(value = "width", required = false) BigDecimal width,
            @RequestParam(value = "dimensionLand", required = false) String dimensionLand,
            @RequestParam(value = "lotNumber", required = false) String lotNumber,
            @RequestParam(value = "propertyUnitTypeId", required = false) Long propertyUnitTypeId,
            @RequestParam(value = "propertyId", required = false) Long propertyId) {
        try {
            PropertyLand updated = propertyLandService.updatePropertyLand(
                    id, landSize, front, back, length, width, dimensionLand, lotNumber,
                    propertyUnitTypeId, propertyId);
            PropertyLandDTO dto = new PropertyLandDTO(updated);
            return ResponseEntity.ok(
                    ApiResponse.success("Property land updated successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update property land: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // ===================== DELETE =====================
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyLandService.deletePropertyLand(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Property land deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property land: " + e.getMessage(), "DELETE_ERROR"));
        }
    }

    // ===================== DELETE BY PROPERTY ID =====================
    @PostMapping("/delete-by-property/{propertyId}")
    public ResponseEntity<ApiResponse<String>> deleteByPropertyId(@PathVariable Long propertyId) {
        try {
            propertyLandService.deleteLandsByPropertyId(propertyId);
            return ResponseEntity.ok(
                    ApiResponse.success("Property lands deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete property lands: " + e.getMessage(), "DELETE_ERROR"));
        }
    }
}
