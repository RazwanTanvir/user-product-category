package com.baylor.practicum_new.controller;

import com.baylor.practicum_new.dto.CategoryDTO;
import com.baylor.practicum_new.dto.ProductDTO;
import com.baylor.practicum_new.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Map<String, Object> categoryDetails) {
        String categoryName = categoryDetails.get("name").toString();
        String description = categoryDetails.get("description").toString();

        CategoryDTO category = categoryService.createCategory(categoryName, description);

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> listAllCategoriesWithProducts() {
        List<CategoryDTO> categoriesWithProducts = categoryService.getAllCategoriesWithProducts();
        return ResponseEntity.ok(categoriesWithProducts);
    }
}
