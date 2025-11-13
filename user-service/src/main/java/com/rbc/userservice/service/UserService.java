package com.rbc.userservice.service;

import com.rbc.userservice.model.User;
import com.rbc.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // CREATE
    @Transactional
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    // READ
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchUsersByFirstName(String firstName) {
        return userRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    public List<User> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    // UPDATE - With partial update support
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Only update non-null fields (partial update support)
        if (userDetails.getUsername() != null) {
            // Check if new username is already taken by another user
            if (!existingUser.getUsername().equals(userDetails.getUsername()) 
                    && userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            existingUser.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null) {
            // Check if new email is already taken by another user
            if (!existingUser.getEmail().equals(userDetails.getEmail()) 
                    && userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existingUser.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }

        if (userDetails.getFirstName() != null) {
            existingUser.setFirstName(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            existingUser.setLastName(userDetails.getLastName());
        }

        if (userDetails.getName() != null) {
            existingUser.setName(userDetails.getName());
        }

        if (userDetails.getNid() != null) {
            existingUser.setNid(userDetails.getNid());
        }

        if (userDetails.getPhone() != null) {
            existingUser.setPhone(userDetails.getPhone());
        }

        if (userDetails.getEnabled() != null) {
            existingUser.setEnabled(userDetails.getEnabled());
        }

        if (userDetails.getStatus() != null) {
            existingUser.setStatus(userDetails.getStatus());
        }

        return userRepository.save(existingUser);
    }

    // DELETE
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}