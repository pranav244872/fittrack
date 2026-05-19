package com.pranav244872.fitness_tracker.service;

import java.util.List;

import com.pranav244872.fitness_tracker.model.Category;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    void deleteCategory(Long id);
}
