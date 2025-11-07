package com.rbc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbc.userservice.model.Permission;
import com.rbc.userservice.model.Permission.PermissionName;
import com.rbc.userservice.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;

/**
 * PermissionService - Business Logic Layer for Permissions
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {
    
    private final PermissionRepository permissionRepository;
    
    /**
     * CREATE - Create new permission
     */
    public Permission createPermission(Permission permission) {
        // Validation: Check duplicate permission name
        if (permissionRepository.existsByName(permission.getName())) {
            throw new RuntimeException("Permission already exists: " + permission.getName());
        }
        
        return permissionRepository.save(permission);
    }
    
    /**
     * READ - Get all permissions
     */
    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
    
    /**
     * READ - Get permission by ID
     */
    @Transactional(readOnly = true)
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }
    
    /**
     * READ - Get permission by name
     */
    @Transactional(readOnly = true)
    public Optional<Permission> getPermissionByName(PermissionName name) {
        return permissionRepository.findByName(name);
    }
    
    /**
     * UPDATE - Update existing permission
     */
    public Permission updatePermission(Long id, Permission permissionDetails) {
        // Find existing permission
        Permission existingPermission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        
        // Check name conflict (if name changed)
        if (!existingPermission.getName().equals(permissionDetails.getName())) {
            if (permissionRepository.existsByName(permissionDetails.getName())) {
                throw new RuntimeException("Permission already exists: " + permissionDetails.getName());
            }
        }
        
        // Update fields
        existingPermission.setName(permissionDetails.getName());
        existingPermission.setDescription(permissionDetails.getDescription());
        
        return permissionRepository.save(existingPermission);
    }
    
    /**
     * DELETE - Delete permission by ID
     */
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }
    
    /**
     * COUNT - Get total permission count
     */
    @Transactional(readOnly = true)
    public long getTotalPermissions() {
        return permissionRepository.count();
    }
}