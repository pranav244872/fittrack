package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.exception.ResourceNotFoundException;
import com.pranav244872.fitness_tracker.model.Category;
import com.pranav244872.fitness_tracker.model.Workout;
import com.pranav244872.fitness_tracker.repository.CategoryRepository;
import com.pranav244872.fitness_tracker.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final CategoryRepository categoryRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, CategoryRepository categoryRepository) {
        this.workoutRepository = workoutRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Workout createWorkout(Long categoryId, Workout workout) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot add workout. Category not found with id: " + categoryId));

        if (workout.getName() == null || workout.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be empty");
        }
        if (workout.getTargetSets() <= 0 || workout.getTargetReps() <= 0) {
            throw new IllegalArgumentException("Sets and Reps must be greater than zero");
        }

        workout.setCategory(category);

        return workoutRepository.save(workout);
    }

    @Override
    public List<Workout> getWorkoutsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return workoutRepository.findByCategoryId(categoryId);
    }

    @Override
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
    }

    @Override
    public void deleteWorkout(Long id) {
        Workout workout = getWorkoutById(id);
        workoutRepository.delete(workout);
    }
}
