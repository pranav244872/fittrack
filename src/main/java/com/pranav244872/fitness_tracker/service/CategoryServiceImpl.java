package com.pranav244872.fitness_tracker.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pranav244872.fitness_tracker.exception.ResourceNotFoundException;
import com.pranav244872.fitness_tracker.model.Category;
import com.pranav244872.fitness_tracker.model.User;
import com.pranav244872.fitness_tracker.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category Name cannot be empty");
        }

		// get the currently logged in user from the security context
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// attach the user to the category
		category.setUser(currentUser);

        return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllCategories() {
        return categoryRepository.findAll();
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	@Override
	public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
	}
}
