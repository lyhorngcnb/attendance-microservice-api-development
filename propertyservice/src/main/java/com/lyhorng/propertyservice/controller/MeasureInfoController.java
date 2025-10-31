package com.lyhorng.propertyservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.PropertyMeasureInfo;
import com.lyhorng.propertyservice.service.MeasureInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/measureinfo")
public class MeasureInfoController {

    @Autowired
    private MeasureInfoService measureInfoService;

    // Route for listing all Measure Info
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PropertyMeasureInfo>>> listAll() {
        List<PropertyMeasureInfo> measureInfoList = measureInfoService.getAllMeasureInfo();
        return ResponseEntity.ok(ApiResponse.success("Measure info fetched successfully.", measureInfoList));
    }

    // Route for creating a new Measure Info
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PropertyMeasureInfo>> create(
            @RequestParam("type") String type,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyMeasureInfo propertyMeasureInfo = measureInfoService.createMeasureInfo(type, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Measure info created successfully.", propertyMeasureInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to create measure info: " + e.getMessage(), null));
        }
    }

    // Route for updating an existing Measure Info
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PropertyMeasureInfo>> update(
            @RequestParam("id") Long id,
            @RequestParam("type") String type,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            PropertyMeasureInfo updatedMeasureInfo = measureInfoService.updateMeasureInfo(id, type, file);
            return ResponseEntity.ok(ApiResponse.success("Measure info updated successfully.", updatedMeasureInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to update measure info: " + e.getMessage(), null));
        }
    }

    // Route for deleting a Measure Info
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> delete(@RequestParam("id") Long id) {
        try {
            measureInfoService.deleteMeasureInfo(id);
            return ResponseEntity.ok(ApiResponse.success("Measure info deleted successfully.", "Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to delete measure info: " + e.getMessage(), null));
        }
    }

    // Route for viewing (getting details of) a Measure Info
    @GetMapping("/view")
    public ResponseEntity<ApiResponse<PropertyMeasureInfo>> view(@RequestParam("id") Long id) {
        try {
            PropertyMeasureInfo measureInfo = measureInfoService.getMeasureInfoById(id);
            return ResponseEntity.ok(ApiResponse.success("Measure info fetched successfully.", measureInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Measure info not found: " + e.getMessage(), null));
        }
    }
}
