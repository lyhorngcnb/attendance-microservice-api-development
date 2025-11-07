package com.rbc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbc.userservice.model.Role;
import com.rbc.userservice.model.Role.RoleName;
import com.rbc.userservice.model.Permission;
import com.rbc.userservice.repository.RoleRepository;
import com.rbc.userservice.repository.PermissionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * RoleService - Business Logic Layer for Roles
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    /**
     * CREATE - Create new role
     */
    public Role createRole(Role role) {
        // Validation: Check duplicate role name
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role already exists: " + role.getName());
        }
        
        // Validate permissions exist
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            Set<Permission> validatedPermissions = new HashSet<>();
            for (Permission permission : role.getPermissions()) {
                Permission existingPermission = permissionRepository.findById(permission.getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permission.getId()));
                validatedPermissions.add(existingPermission);
            }
            role.setPermissions(validatedPermissions);
        }
        
        return roleRepository.save(role);
    }
    
    /**
     * READ - Get all roles
     */
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    /**
     * READ - Get role by ID
     */
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
    
    /**
     * READ - Get role by name
     */
    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(RoleName name) {
        return roleRepository.findByName(name);
    }
    
    /**
     * UPDATE - Update existing role
     */
    public Role updateRole(Long id, Role roleDetails) {
        // Find existing role
        Role existingRole = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        // Check name conflict (if name changed)
        if (!existingRole.getName().equals(roleDetails.getName())) {
            if (roleRepository.existsByName(roleDetails.getName())) {
                throw new RuntimeException("Role already exists: " + roleDetails.getName());
            }
        }
        
        // Update fields
        existingRole.setName(roleDetails.getName());
        existingRole.setDescription(roleDetails.getDescription());
        
        // Update permissions if provided
        if (roleDetails.getPermissions() != null) {
            Set<Permission> validatedPermissions = new HashSet<>();
            for (Permission permission : roleDetails.getPermissions()) {
                Permission existingPermission = permissionRepository.findById(permission.getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permission.getId()));
                validatedPermissions.add(existingPermission);
            }
            existingRole.setPermissions(validatedPermissions);
        }
        
        return roleRepository.save(existingRole);
    }
    
    /**
     * DELETE - Delete role by ID
     */
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
    
    /**
     * ADD PERMISSION TO ROLE
     */
    public Role addPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        
        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }
    
    /**
     * REMOVE PERMISSION FROM ROLE
     */
    public Role removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        
        role.getPermissions().remove(permission);
        return roleRepository.save(role);
    }
    
    /**
     * GET ROLES BY PERMISSION
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesByPermissionId(Long permissionId) {
        return roleRepository.findByPermissionId(permissionId);
    }
    
    /**
     * COUNT - Get total role count
     */
    @Transactional(readOnly = true)
    public long getTotalRoles() {
        return roleRepository.count();
    }
}