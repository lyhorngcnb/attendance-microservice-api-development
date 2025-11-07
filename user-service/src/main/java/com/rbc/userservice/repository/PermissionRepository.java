package com.rbc.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rbc.userservice.model.Permission;
import com.rbc.userservice.model.Permission.PermissionName;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * Find permission by name
     * Generated SQL: SELECT * FROM permissions WHERE name = ?
     */
    Optional<Permission> findByName(PermissionName name);
    
    /**
     * Check if permission name exists
     */
    boolean existsByName(PermissionName name);
}