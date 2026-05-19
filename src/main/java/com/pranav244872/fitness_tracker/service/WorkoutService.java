package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.model.Workout;
import java.util.List;

public interface WorkoutService {
    Workout createWorkout(Long categoryId, Workout workout);
    List<Workout> getWorkoutsByCategoryId(Long categoryId);
    Workout getWorkoutById(Long id);
    void deleteWorkout(Long id);
}

