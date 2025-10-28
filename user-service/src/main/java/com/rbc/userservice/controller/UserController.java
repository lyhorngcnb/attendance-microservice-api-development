package com.rbc.userservice.controller;

import com.lyhorng.common.response.ApiResponse;
import com.rbc.userservice.model.User;
import com.rbc.userservice.service.UserService;

import jakarta.validation.Valid;
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

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        try {
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

    // READ ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
    }

    // READ BY USERNAME
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
    }

    // READ BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found", "USER_NOT_FOUND")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
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

    // SEARCH BY FIRST NAME
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(@RequestParam String firstName) {
        List<User> users = userService.searchUsersByFirstName(firstName);
        return ResponseEntity.ok(ApiResponse.success("Users found", users));
    }

    // GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByStatus(@PathVariable User.UserStatus status) {
        List<User> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    // COUNT
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = userService.getTotalUsers();
        return ResponseEntity.ok(ApiResponse.success("User count retrieved successfully", count));
    }
}
