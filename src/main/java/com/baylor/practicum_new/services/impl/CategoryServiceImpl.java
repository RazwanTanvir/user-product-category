package com.baylor.practicum_new.services.impl;

import com.baylor.practicum_new.dto.CategoryDTO;
import com.baylor.practicum_new.dto.ProductDTO;
import com.baylor.practicum_new.entities.Category;
import com.baylor.practicum_new.entities.Product;
import com.baylor.practicum_new.entities.User;
import com.baylor.practicum_new.repositories.CategoryRepository;
import com.baylor.practicum_new.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public CategoryDTO createCategory(String name, String description) {
//        Category category = categoryRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory.getCategoryId(), savedCategory.getName(), savedCategory.getDescription(), savedCategory.getProducts());
    }

    @Override
    public List<CategoryDTO> getAllCategoriesWithProducts() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::mapCategoryToDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO mapCategoryToDTO(Category category) {

        return new CategoryDTO(category.getCategoryId(), category.getName(), category.getDescription(), category.getProducts());
    }
}
