package com.rbc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbc.userservice.model.User;
import com.rbc.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserService - Business Logic Layer
 * 
 * Handles business rules, validation, and coordinates repository operations
 * 
 * @Service - Marks as service component (Spring manages it)
 * @RequiredArgsConstructor - Lombok: generates constructor for final fields
 * @Transactional - Ensures database transactions (rollback on error)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    /**
     * Dependency Injection
     * Spring automatically injects UserRepository instance
     * 'final' ensures it cannot be changed after initialization
     */
    private final UserRepository userRepository;
    
    /**
     * CREATE - Create new user
     * 
     * Business Logic:
     * 1. Check if username/email already exists
     * 2. Save user to database
     * 
     * @param user - User object to create
     * @return Created user with generated ID
     * @throws RuntimeException if username/email exists
     */
    public User createUser(User user) {
        // Validation: Check duplicate username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        // Validation: Check duplicate email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Save and return (with generated ID)
        return userRepository.save(user);
    }
    
    /**
     * READ - Get all users
     * 
     * @return List of all users
     */
    @Transactional(readOnly = true) // Optimization for read operations
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * READ - Get user by ID
     * 
     * @param id - User ID
     * @return Optional containing user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * READ - Get user by username
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * READ - Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * UPDATE - Update existing user
     * 
     * Business Logic:
     * 1. Check if user exists
     * 2. Check if new username/email conflicts with other users
     * 3. Update fields
     * 4. Save changes
     * 
     * @param id - User ID to update
     * @param userDetails - New user data
     * @return Updated user
     * @throws RuntimeException if user not found or conflicts exist
     */
    public User updateUser(Long id, User userDetails) {
        // Find existing user
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check username conflict (if username changed)
        if (!existingUser.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username already exists: " + userDetails.getUsername());
            }
        }
        
        // Check email conflict (if email changed)
        if (!existingUser.getEmail().equals(userDetails.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + userDetails.getEmail());
            }
        }
        
        // Update fields
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        // existingUser.setStatus(userDetails.getStatus());
        
        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }
        
        // Save and return
        return userRepository.save(existingUser);
    }
    
    /**
     * DELETE - Delete user by ID
     * 
     * @param id - User ID to delete
     * @throws RuntimeException if user not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * SEARCH - Find users by status
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }
    
    /**
     * SEARCH - Search users by first name
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByFirstName(String firstName) {
        return userRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
    
    /**
     * COUNT - Get total user count
     */
    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
    }
}