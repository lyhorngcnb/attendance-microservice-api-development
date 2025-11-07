package com.rbc.userservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.rbc.userservice.model.Permission;
import com.rbc.userservice.model.Permission.PermissionName;
import com.rbc.userservice.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<Permission>> createPermission(@Valid @RequestBody Permission permission) {
        try {
            Permission createdPermission = permissionService.createPermission(permission);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Permission created successfully", createdPermission));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Failed to create permission: " + e.getMessage(), "PERMISSION_CONFLICT"));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success("Permissions retrieved successfully", permissions));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable Long id) {
        return permissionService.getPermissionById(id)
                .map(permission -> ResponseEntity.ok(ApiResponse.success("Permission found", permission)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Permission not found", "PERMISSION_NOT_FOUND")));
    }

    // READ BY NAME
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Permission>> getPermissionByName(@PathVariable PermissionName name) {
        return permissionService.getPermissionByName(name)
                .map(permission -> ResponseEntity.ok(ApiResponse.success("Permission found", permission)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Permission not found", "PERMISSION_NOT_FOUND")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(
            @PathVariable Long id, @Valid @RequestBody Permission permissionDetails) {
        try {
            Permission updatedPermission = permissionService.updatePermission(id, permissionDetails);
            return ResponseEntity.ok(ApiResponse.success("Permission updated successfully", updatedPermission));
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;
            String errorCode = e.getMessage().contains("not found") ? "PERMISSION_NOT_FOUND" : "PERMISSION_CONFLICT";
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(e.getMessage(), errorCode));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok(ApiResponse.successNoData("Permission deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Permission not found", "PERMISSION_NOT_FOUND"));
        }
    }

    // COUNT
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getPermissionCount() {
        long count = permissionService.getTotalPermissions();
        return ResponseEntity.ok(ApiResponse.success("Permission count retrieved successfully", count));
    }
}