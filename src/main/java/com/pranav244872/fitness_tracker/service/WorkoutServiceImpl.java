package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.WorkoutResponse;
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
    public WorkoutResponse createWorkout(Long categoryId, Workout workout) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot add workout. Category not found with id: " + categoryId));

        if (workout.getName() == null || workout.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be empty");
        }
        if (workout.getTargetSets() <= 0 || workout.getTargetReps() <= 0) {
            throw new IllegalArgumentException("Sets and Reps must be greater than zero");
        }

        workout.setCategory(category);
        Workout saved = workoutRepository.save(workout);
        return toResponse(saved);
    }

    @Override
    public List<WorkoutResponse> getWorkoutsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return workoutRepository.findByCategoryIdOrderByIdAsc(categoryId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public WorkoutResponse getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        return toResponse(workout);
    }

    @Override
    public WorkoutResponse updateWorkout(Long id, Workout workoutDetails) {
        Workout workout = getWorkoutEntityById(id);
        
        if (workoutDetails.getName() == null || workoutDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be empty");
        }
        if (workoutDetails.getTargetSets() <= 0 || workoutDetails.getTargetReps() <= 0) {
            throw new IllegalArgumentException("Sets and Reps must be greater than zero");
        }

        workout.setName(workoutDetails.getName());
        workout.setTargetSets(workoutDetails.getTargetSets());
        workout.setTargetReps(workoutDetails.getTargetReps());
        workout.setRestBetweenSetsSeconds(workoutDetails.getRestBetweenSetsSeconds());

        Workout updated = workoutRepository.save(workout);
        return toResponse(updated);
    }

    @Override
    public void deleteWorkout(Long id) {
        Workout workout = getWorkoutEntityById(id);
        workoutRepository.delete(workout);
    }

    private Workout getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
    }

    private WorkoutResponse toResponse(Workout w) {
        return new WorkoutResponse(w.getId(), w.getName(), w.getTargetSets(), w.getTargetReps(), w.getRestBetweenSetsSeconds());
    }
}
