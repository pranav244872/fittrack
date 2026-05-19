package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.exception.ResourceNotFoundException;
import com.pranav244872.fitness_tracker.model.Category;
import com.pranav244872.fitness_tracker.model.WorkoutLog;
import com.pranav244872.fitness_tracker.repository.CategoryRepository;
import com.pranav244872.fitness_tracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final CategoryRepository categoryRepository;

    // Manual constructor injection
    public WorkoutLogServiceImpl(WorkoutLogRepository workoutLogRepository, CategoryRepository categoryRepository) {
        this.workoutLogRepository = workoutLogRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public WorkoutLog logWorkout(Long categoryId, int durationMinutes) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot log workout. Category not found with id: " + categoryId));

        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Workout duration must be greater than zero minutes");
        }

        WorkoutLog log = new WorkoutLog();
        log.setCompletionDate(LocalDateTime.now());
        log.setDurationMinutes(durationMinutes);
        log.setCategory(category);

        return workoutLogRepository.save(log);
    }

    @Override
    public List<WorkoutLog> getAllWorkoutLogs() {
        return workoutLogRepository.findAll();
    }

    @Override
    public void deleteWorkoutLog(Long id) {
        if (!workoutLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout log not found with id: " + id);
        }
        workoutLogRepository.deleteById(id);
    }
}
