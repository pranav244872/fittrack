package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.model.WorkoutLog;
import java.util.List;

public interface WorkoutLogService {
    WorkoutLog logWorkout(Long categoryId, int durationMinutes);
    List<WorkoutLog> getAllWorkoutLogs();
    void deleteWorkoutLog(Long id);
}
