package com.pranav244872.fitness_tracker.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pranav244872.fitness_tracker.dto.CategoryResponse;
import com.pranav244872.fitness_tracker.dto.WorkoutResponse;
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
	public CategoryResponse createCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category Name cannot be empty");
        }

		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		category.setUser(currentUser);

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
	}

	@Override
	public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
	}

	@Override
	public CategoryResponse getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return toResponse(category);
	}

	@Override
	public CategoryResponse updateCategory(Long id, Category categoryDetails) {
		Category category = getCategoryByIdEntity(id);
		if (categoryDetails.getName() == null || categoryDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category Name cannot be empty");
        }
		category.setName(categoryDetails.getName());
		Category updated = categoryRepository.save(category);
		return toResponse(updated);
	}

	@Override
	public void deleteCategory(Long id) {
        Category category = getCategoryByIdEntity(id);
        categoryRepository.delete(category);
	}

    private Category getCategoryByIdEntity(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryResponse toResponse(Category c) {
        List<WorkoutResponse> workouts = c.getWorkouts() == null ? List.of() :
            c.getWorkouts().stream()
                .map(w -> new WorkoutResponse(w.getId(), w.getName(), w.getTargetSets(), w.getTargetReps(), w.getRestBetweenSetsSeconds()))
                .toList();
        return new CategoryResponse(c.getId(), c.getName(), workouts);
    }
}
