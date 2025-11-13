package com.rbc.userservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.rbc.userservice.model.User;
import com.rbc.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE - Using @RequestParam
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String name,
            @RequestParam String nid,
            @RequestParam String phone,
            @RequestParam Boolean enabled,
            @RequestParam User.UserStatus status) {
        try {
            User user = new User(username, email, password, firstName, lastName, name, nid, phone, enabled, status);
            User createdUser = userService.createUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Failed to create user: " + e.getMessage(), "USER_CONFLICT"));
        }
    }

    // SMART READ - Handles all GET operations with optional filters
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) User.UserStatus status) {
        
        // Filter by ID (single user)
        if (id != null) {
            return userService.getUserById(id)
                    .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
        }
        
        // Filter by username (single user)
        if (username != null) {
            return userService.getUserByUsername(username)
                    .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
        }
        
        // Filter by email (single user)
        if (email != null) {
            return userService.getUserByEmail(email)
                    .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
        }
        
        // Filter by firstName (multiple users)
        if (firstName != null) {
            List<User> users = userService.searchUsersByFirstName(firstName);
            return ResponseEntity.ok(ApiResponse.success("Users found", users));
        }
        
        // Filter by status (multiple users)
        if (status != null) {
            List<User> users = userService.getUsersByStatus(status);
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
        }
        
        // No filters - return all users
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    // UPDATE - Using @RequestParam with partial updates support
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nid,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) User.UserStatus status) {
        try {
            // Create user object with only provided fields
            User userDetails = new User();
            userDetails.setUsername(username);
            userDetails.setEmail(email);
            userDetails.setPassword(password);
            userDetails.setFirstName(firstName);
            userDetails.setLastName(lastName);
            userDetails.setName(name);
            userDetails.setNid(nid);
            userDetails.setPhone(phone);
            userDetails.setEnabled(enabled);
            userDetails.setStatus(status);
            
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found", "USER_NOT_FOUND"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.successNoData("User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found", "USER_NOT_FOUND"));
        }
    }

    // COUNT
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = userService.getTotalUsers();
        return ResponseEntity.ok(ApiResponse.success("User count retrieved successfully", count));
    }
}