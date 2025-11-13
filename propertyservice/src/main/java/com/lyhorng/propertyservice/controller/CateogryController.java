package com.lyhorng.propertyservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.propertyservice.model.Category;
import com.lyhorng.propertyservice.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CateogryController {

    @Autowired
    public CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Category>>> listAll() {
        List<Category> categoryList = categoryService.getAllCategory();
        return ResponseEntity.ok(ApiResponse.success("Category fetch success!", categoryList));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Category>> create(
            @RequestParam("categoryName") String category) {
        try {
            Category categoryName = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.success("Category is created successfull!", categoryName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create category: " + e.getMessage(), null));

        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Category>> update(
        @RequestParam("id") Long id,
        @RequestParam("categoryName") String categoryName
    ) {
        try {
            Category updateCategory = categoryService.updateCategory(id, categoryName);
            return ResponseEntity.ok(ApiResponse.success("Category Update Successfull", updateCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("Fail to update Category:"+ e.getMessage(), null)
           );
        }
    }
    
}
