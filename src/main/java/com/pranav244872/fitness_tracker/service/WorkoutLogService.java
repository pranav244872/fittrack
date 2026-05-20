package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.WorkoutLogResponse;
import com.pranav244872.fitness_tracker.model.WorkoutLog;
import java.util.List;
import java.time.LocalDateTime;
import java.time.YearMonth;

public interface WorkoutLogService {
    WorkoutLogResponse logWorkout(Long categoryId, int durationMinutes);
    List<WorkoutLogResponse> getAllWorkoutLogs();
    List<WorkoutLogResponse> getWorkoutLogsByMonth(int year, int month, Long userId);
    void deleteWorkoutLog(Long id);
}
