package com.lyhorng.customerservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lyhorng.customerservice.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long id);
    Optional<Customer> findByFirstName(String firstName);
    Optional<Customer> findByLastName(String lastName);
    
} 