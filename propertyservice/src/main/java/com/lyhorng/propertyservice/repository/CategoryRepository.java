package com.lyhorng.propertyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lyhorng.propertyservice.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}