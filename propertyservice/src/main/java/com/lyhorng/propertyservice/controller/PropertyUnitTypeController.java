package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.dto.PropertyUnitTypeDTO;
import com.lyhorng.propertyservice.model.PropertyUnitType;
import com.lyhorng.propertyservice.service.PropertyUnitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/property-unit-types")
@CrossOrigin(origins = "*")
public class PropertyUnitTypeController {

    @Autowired
    private PropertyUnitTypeService propertyUnitTypeService;

    // ===================== LIST ALL =====================
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyUnitTypeDTO>>> listAll() {
        try {
            List<PropertyUnitType> unitTypes = propertyUnitTypeService.getAllUnitTypes();
            List<PropertyUnitTypeDTO> dtos = unitTypes.stream()
                    .map(PropertyUnitTypeDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Property unit types retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve unit types: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== LIST ACTIVE =====================
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PropertyUnitTypeDTO>>> listActive() {
        try {
            List<PropertyUnitType> unitTypes = propertyUnitTypeService.getActiveUnitTypes();
            List<PropertyUnitTypeDTO> dtos = unitTypes.stream()
                    .map(PropertyUnitTypeDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    ApiResponse.success("Active unit types retrieved successfully", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve unit types: " + e.getMessage(), "LIST_ERROR"));
        }
    }

    // ===================== VIEW =====================
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyUnitTypeDTO>> view(@RequestParam("id") Long id) {
        try {
            PropertyUnitType unitType = propertyUnitTypeService.getUnitTypeById(id);
            PropertyUnitTypeDTO dto = new PropertyUnitTypeDTO(unitType);
            return ResponseEntity.ok(
                    ApiResponse.success("Unit type retrieved successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Unit type not found: " + e.getMessage(), "VIEW_ERROR"));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyUnitTypeDTO>> create(
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "conversionToSqm", required = false) BigDecimal conversionToSqm,
            @RequestParam(value = "isActive", required = false) Boolean isActive) {
        try {
            PropertyUnitType created = propertyUnitTypeService.createUnitType(
                    name, code, description, conversionToSqm, isActive);
            PropertyUnitTypeDTO dto = new PropertyUnitTypeDTO(created);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Unit type created successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create unit type: " + e.getMessage(), "CREATE_ERROR"));
        }
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyUnitTypeDTO>> update(
            @RequestParam("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "conversionToSqm", required = false) BigDecimal conversionToSqm,
            @RequestParam(value = "isActive", required = false) Boolean isActive) {
        try {
            PropertyUnitType updated = propertyUnitTypeService.updateUnitType(
                    id, name, code, description, conversionToSqm, isActive);
            PropertyUnitTypeDTO dto = new PropertyUnitTypeDTO(updated);
            return ResponseEntity.ok(
                    ApiResponse.success("Unit type updated successfully", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update unit type: " + e.getMessage(), "UPDATE_ERROR"));
        }
    }

    // ===================== DELETE =====================
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            propertyUnitTypeService.deleteUnitType(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Unit type deleted successfully", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete unit type: " + e.getMessage(), "DELETE_ERROR"));
        }
    }
}