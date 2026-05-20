package com.pranav244872.fitness_tracker.service;

import java.util.List;

import com.pranav244872.fitness_tracker.dto.CategoryResponse;
import com.pranav244872.fitness_tracker.model.Category;

public interface CategoryService {
    CategoryResponse createCategory(Category category);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse updateCategory(Long id, Category categoryDetails);
    void deleteCategory(Long id);
}
