package com.rbc.userservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbc.userservice.model.User;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Data Access Layer
 * 
 * CRITICAL DEPENDENCY: spring-boot-starter-data-jpa
 * 
 * JpaRepository provides automatic CRUD operations:
 * - save(entity) - Create/Update
 * - findById(id) - Read by ID
 * - findAll() - Read all
 * - deleteById(id) - Delete
 * - count() - Count records
 * - existsById(id) - Check existence
 * 
 * Spring Data JPA automatically implements this interface!
 * No need to write implementation code.
 * 
 * @Repository - Marks as data access component
 * extends JpaRepository<Entity, IDType> - Entity=User, ID=Long
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Custom query methods using Spring Data JPA naming convention
     * Spring automatically generates SQL from method names!
     * 
     * Pattern: findBy + PropertyName + Condition
     */
    
    /**
     * Find user by username
     * Generated SQL: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * Generated SQL: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if username exists
     * Generated SQL: SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by status
     * Generated SQL: SELECT * FROM users WHERE status = ?
     */
    List<User> findByStatus(User.UserStatus status);
    
    /**
     * Find users by first name containing (case-insensitive search)
     * Generated SQL: SELECT * FROM users WHERE LOWER(first_name) LIKE LOWER(?)
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Custom JPQL query - for complex queries
     * @Query - Allows writing custom JPQL/SQL
     * :param - Named parameter binding
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = :status")
    Optional<User> findByEmailAndStatus(@Param("email") String email, 
                                        @Param("status") User.UserStatus status);
    
    /**
     * Native SQL query example
     * nativeQuery = true - Uses raw SQL instead of JPQL
     */
    @Query(value = "SELECT * FROM users WHERE created_at > DATE_SUB(NOW(), INTERVAL 30 DAY)", 
           nativeQuery = true)
    List<User> findRecentUsers();
}