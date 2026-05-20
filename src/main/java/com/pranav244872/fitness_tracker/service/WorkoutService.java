package com.pranav244872.fitness_tracker.service;

import java.util.List;

import com.pranav244872.fitness_tracker.dto.WorkoutResponse;
import com.pranav244872.fitness_tracker.model.Workout;

public interface WorkoutService {
    WorkoutResponse createWorkout(Long categoryId, Workout workout);
    List<WorkoutResponse> getWorkoutsByCategoryId(Long categoryId);
    WorkoutResponse getWorkoutById(Long id);
    WorkoutResponse updateWorkout(Long id, Workout workoutDetails);
    void deleteWorkout(Long id);
}

