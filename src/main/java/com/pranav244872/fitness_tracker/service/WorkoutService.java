package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.WorkoutResponse;
import com.pranav244872.fitness_tracker.model.Workout;
import java.util.List;

public interface WorkoutService {
    WorkoutResponse createWorkout(Long categoryId, Workout workout);
    List<WorkoutResponse> getWorkoutsByCategoryId(Long categoryId);
    WorkoutResponse getWorkoutById(Long id);
    void deleteWorkout(Long id);
}
