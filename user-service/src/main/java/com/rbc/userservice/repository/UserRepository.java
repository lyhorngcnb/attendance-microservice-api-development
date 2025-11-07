package com.rbc.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rbc.userservice.model.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByStatus(User.UserStatus status);
    
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * NEW: Find user by phone number
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * NEW: Find user by NID
     */
    Optional<User> findByNid(String nid);
    
    /**
     * NEW: Find users by enabled status
     */
    List<User> findByEnabled(boolean enabled);
    
    /**
     * NEW: Check if phone exists
     */
    boolean existsByPhone(String phone);
    
    /**
     * NEW: Check if NID exists
     */
    boolean existsByNid(String nid);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = :status")
    Optional<User> findByEmailAndStatus(@Param("email") String email, 
                                        @Param("status") User.UserStatus status);
    
    @Query(value = "SELECT * FROM users WHERE created_at > DATE_SUB(NOW(), INTERVAL 30 DAY)", 
           nativeQuery = true)
    List<User> findRecentUsers();
}