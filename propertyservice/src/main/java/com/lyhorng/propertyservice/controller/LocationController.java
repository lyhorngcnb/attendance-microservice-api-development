package com.lyhorng.propertyservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.Commune;
import com.lyhorng.propertyservice.model.District;
import com.lyhorng.propertyservice.model.Province;
import com.lyhorng.propertyservice.model.Village;
import com.lyhorng.propertyservice.service.LocationService;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // ===================== GET ALL PROVINCES =====================
    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<List<Province>>> getAllProvinces() {
        try {
            List<Province> provinces = locationService.getAllProvinces();
            return ResponseEntity.ok(
                    ApiResponse.success("Provinces retrieved successfully", provinces));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve provinces: " + e.getMessage(), "PROVINCE_ERROR"));
        }
    }

    // ===================== GET DISTRICTS BY PROVINCE =====================
    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<District>>> getDistricts(
            @RequestParam(required = false) Long provinceId) {
        try {
            List<District> districts;
            if (provinceId != null) {
                // Get districts filtered by province
                districts = locationService.getDistrictsByProvinceId(provinceId);
            } else {
                // Get all districts
                districts = locationService.getAllDistricts();
            }
            return ResponseEntity.ok(
                    ApiResponse.success("Districts retrieved successfully", districts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve districts: " + e.getMessage(), "DISTRICT_ERROR"));
        }
    }

    // ===================== GET COMMUNES BY DISTRICT =====================
    @GetMapping("/communes")
    public ResponseEntity<ApiResponse<List<Commune>>> getCommunes(
            @RequestParam(required = false) Long districtId) {
        try {
            List<Commune> communes;
            if (districtId != null) {
                // Get communes filtered by district
                communes = locationService.getCommunesByDistrictId(districtId);
            } else {
                // Get all communes
                communes = locationService.getAllCommunes();
            }
            return ResponseEntity.ok(
                    ApiResponse.success("Communes retrieved successfully", communes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve communes: " + e.getMessage(), "COMMUNE_ERROR"));
        }
    }

    // ===================== GET VILLAGES BY COMMUNE =====================
    @GetMapping("/villages")
    public ResponseEntity<ApiResponse<List<Village>>> getVillages(
            @RequestParam(required = false) Long communeId) {
        try {
            List<Village> villages;
            if (communeId != null) {
                // Get villages filtered by commune
                villages = locationService.getVillagesByCommuneId(communeId);
            } else {
                // Get all villages
                villages = locationService.getAllVillages();
            }
            return ResponseEntity.ok(
                    ApiResponse.success("Villages retrieved successfully", villages));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve villages: " + e.getMessage(), "VILLAGE_ERROR"));
        }
    }

    // ===================== GET COMPLETE HIERARCHY =====================
    // Optional: Get complete location hierarchy in one call
    @GetMapping("/hierarchy")
    public ResponseEntity<ApiResponse<LocationHierarchyDTO>> getLocationHierarchy(
            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long communeId) {
        try {
            LocationHierarchyDTO hierarchy = new LocationHierarchyDTO();
            
            // Always get all provinces
            hierarchy.setProvinces(locationService.getAllProvinces());
            
            // Get districts if province is selected
            if (provinceId != null) {
                hierarchy.setDistricts(locationService.getDistrictsByProvinceId(provinceId));
            }
            
            // Get communes if district is selected
            if (districtId != null) {
                hierarchy.setCommunes(locationService.getCommunesByDistrictId(districtId));
            }
            
            // Get villages if commune is selected
            if (communeId != null) {
                hierarchy.setVillages(locationService.getVillagesByCommuneId(communeId));
            }
            
            return ResponseEntity.ok(
                    ApiResponse.success("Location hierarchy retrieved successfully", hierarchy));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve location hierarchy: " + e.getMessage(), "HIERARCHY_ERROR"));
        }
    }

    // Inner DTO class for hierarchy response
    public static class LocationHierarchyDTO {
        private List<Province> provinces;
        private List<District> districts;
        private List<Commune> communes;
        private List<Village> villages;

        public List<Province> getProvinces() { return provinces; }
        public void setProvinces(List<Province> provinces) { this.provinces = provinces; }
        
        public List<District> getDistricts() { return districts; }
        public void setDistricts(List<District> districts) { this.districts = districts; }
        
        public List<Commune> getCommunes() { return communes; }
        public void setCommunes(List<Commune> communes) { this.communes = communes; }
        
        public List<Village> getVillages() { return villages; }
        public void setVillages(List<Village> villages) { this.villages = villages; }
    }
}