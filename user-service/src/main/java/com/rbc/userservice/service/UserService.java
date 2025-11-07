package com.rbc.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbc.userservice.model.User;
import com.rbc.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * CREATE - Create new user
     * UPDATED: Includes validation for new fields
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
        
        // Set default values if not provided
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        
        if (user.getStatus() == null) {
            user.setStatus(User.UserStatus.ACTIVE);
        }
        
        // Save and return (with generated ID)
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * UPDATE - Update existing user
     * UPDATED: Includes new fields
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
        existingUser.setName(userDetails.getName());
        existingUser.setNid(userDetails.getNid());
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setEnabled(userDetails.getEnabled());
        existingUser.setStatus(userDetails.getStatus());
        
        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }
        
        return userRepository.save(existingUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<User> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<User> searchUsersByFirstName(String firstName) {
        return userRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
    
    /**
     * NEW: Search users by phone
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
    
    /**
     * NEW: Search users by NID
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByNid(String nid) {
        return userRepository.findByNid(nid);
    }
    
    /**
     * NEW: Get enabled/disabled users
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByEnabled(boolean enabled) {
        return userRepository.findByEnabled(enabled);
    }
    
    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
    }
}