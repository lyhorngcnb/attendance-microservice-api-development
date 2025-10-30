package com.lyhorng.customerservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.customerservice.model.Customer;
import com.lyhorng.customerservice.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    // Create new customer
    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> createCustomer(
        @Valid @RequestBody Customer customer
    ) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", createdCustomer));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.error("Failed to create customer: " + e.getMessage(), "USER_CONFLICT")
            );
        }
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
            .map(customer -> ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", customer)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Customer not found with id: " + id, "USER_NOT_FOUND")
            ));
    }

    // Update customer
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody Customer updatedCustomer
    ) {
        try {
            Customer customer = customerService.updateCustomer(id, updatedCustomer);
            return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", customer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Customer not found or failed to update: " + e.getMessage(), "USER_NOT_FOUND")
            );
        }
    }

    // Delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ApiResponse.success("Customer deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Customer not found or failed to delete: " + e.getMessage(), "USER_NOT_FOUND")
            );
        }
    }
}
