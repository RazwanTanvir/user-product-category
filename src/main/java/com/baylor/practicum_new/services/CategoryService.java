package com.baylor.practicum_new.services;

import com.baylor.practicum_new.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(String name, String description);

    public List<CategoryDTO> getAllCategoriesWithProducts();
}
