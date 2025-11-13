package com.lyhorng.propertyservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lyhorng.propertyservice.model.Category;
import com.lyhorng.propertyservice.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    public CategoryRepository categoryRepository;

    // List
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    // View
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category is Not Found ID :" + id));
    }

    // Create
    public Category createCategory(String categoryName) {
        Category category = new Category();
        category.setCategory(categoryName);
        return categoryRepository.save(category);
    }

    // Update
    public Category updateCategory(Long id, String categoryName) {
        Optional<Category> exitingCategory = categoryRepository.findById(id);
        if (exitingCategory.isPresent()) {
            Category category = exitingCategory.get();
            category.setCategory(categoryName);
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category is not found ID:" + id);
        }

    }
    // Delete

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
