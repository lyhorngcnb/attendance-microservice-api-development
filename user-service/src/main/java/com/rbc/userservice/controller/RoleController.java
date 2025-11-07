package com.rbc.userservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.rbc.userservice.model.Role;
import com.rbc.userservice.model.Role.RoleName;
import com.rbc.userservice.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Role created successfully", createdRole));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Failed to create role: " + e.getMessage(), "ROLE_CONFLICT"));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success("Roles retrieved successfully", roles));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(role -> ResponseEntity.ok(ApiResponse.success("Role found", role)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Role not found", "ROLE_NOT_FOUND")));
    }

    // READ BY NAME
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Role>> getRoleByName(@PathVariable RoleName name) {
        return roleService.getRoleByName(name)
                .map(role -> ResponseEntity.ok(ApiResponse.success("Role found", role)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Role not found", "ROLE_NOT_FOUND")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(
            @PathVariable Long id, @Valid @RequestBody Role roleDetails) {
        try {
            Role updatedRole = roleService.updateRole(id, roleDetails);
            return ResponseEntity.ok(ApiResponse.success("Role updated successfully", updatedRole));
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;
            String errorCode = e.getMessage().contains("Role not found") ? "ROLE_NOT_FOUND" : 
                              e.getMessage().contains("Permission not found") ? "PERMISSION_NOT_FOUND" : "ROLE_CONFLICT";
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(e.getMessage(), errorCode));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(ApiResponse.successNoData("Role deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Role not found", "ROLE_NOT_FOUND"));
        }
    }

    // ADD PERMISSION TO ROLE
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<ApiResponse<Role>> addPermissionToRole(
            @PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            Role updatedRole = roleService.addPermissionToRole(roleId, permissionId);
            return ResponseEntity.ok(ApiResponse.success("Permission added to role successfully", updatedRole));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "NOT_FOUND"));
        }
    }

    // REMOVE PERMISSION FROM ROLE
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<ApiResponse<Role>> removePermissionFromRole(
            @PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            Role updatedRole = roleService.removePermissionFromRole(roleId, permissionId);
            return ResponseEntity.ok(ApiResponse.success("Permission removed from role successfully", updatedRole));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "NOT_FOUND"));
        }
    }

    // GET ROLES BY PERMISSION
    @GetMapping("/by-permission/{permissionId}")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesByPermission(@PathVariable Long permissionId) {
        List<Role> roles = roleService.getRolesByPermissionId(permissionId);
        return ResponseEntity.ok(ApiResponse.success("Roles retrieved successfully", roles));
    }

    // COUNT
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getRoleCount() {
        long count = roleService.getTotalRoles();
        return ResponseEntity.ok(ApiResponse.success("Role count retrieved successfully", count));
    }
}