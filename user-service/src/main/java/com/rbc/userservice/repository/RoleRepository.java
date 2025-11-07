package com.rbc.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rbc.userservice.model.Role;
import com.rbc.userservice.model.Role.RoleName;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name
     * Generated SQL: SELECT * FROM roles WHERE name = ?
     */
    Optional<Role> findByName(RoleName name);
    
    /**
     * Check if role name exists
     */
    boolean existsByName(RoleName name);
    
    /**
     * Find roles that contain a specific permission
     * Uses JPQL with JOIN to fetch roles with specific permission
     */
    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.id = :permissionId")
    List<Role> findByPermissionId(@Param("permissionId") Long permissionId);
}