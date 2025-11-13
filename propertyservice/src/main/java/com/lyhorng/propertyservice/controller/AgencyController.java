package com.lyhorng.propertyservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.Agency;
import com.lyhorng.propertyservice.service.AgencyService;

@RestController
@RequestMapping("/api/agency")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Agency>>> listAll() {
        List<Agency> agencyList = agencyService.getAllAgency();
        return ResponseEntity.ok(ApiResponse.success("Agency fetch success.", agencyList));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Agency>> create(
            @RequestParam("agency_name") String agency_name) {
        try {
            Agency agency = agencyService.createAgency(agency_name);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Agency created successfull.", agency));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create agency: " + e.getMessage(), null));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Agency>> update(
        @RequestParam("id") Long id,
        @RequestParam("agency_name") String agency_name
    ) {
        try {
            Agency updateAgency = agencyService.updateAgency(id, agency_name);
            return ResponseEntity.ok(ApiResponse.success("Agency update Successful.", updateAgency));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Failed to update agency: " + e.getMessage(), null));
        }
    }

    @GetMapping("/view")
    public ResponseEntity<ApiResponse<Agency>> view(@RequestParam("id") Long id) {
        try {
            Agency agency = agencyService.getAgency(id);
            return ResponseEntity.ok(ApiResponse.success("Agency info fetc successfull.", agency));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Agency not found: " + e.getMessage(), null));
        }
    }
}
