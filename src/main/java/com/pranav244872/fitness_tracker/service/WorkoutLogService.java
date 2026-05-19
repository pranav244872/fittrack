package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.WorkoutLogResponse;
import com.pranav244872.fitness_tracker.model.WorkoutLog;
import java.util.List;

public interface WorkoutLogService {
    WorkoutLogResponse logWorkout(Long categoryId, int durationMinutes);
    List<WorkoutLogResponse> getAllWorkoutLogs();
    void deleteWorkoutLog(Long id);
}
