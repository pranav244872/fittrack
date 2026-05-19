package com.pranav244872.fitness_tracker.dto;

import java.time.LocalDateTime;

public record WorkoutLogResponse(
    Long id,
    int durationMinutes,
    LocalDateTime completionDate,
    Long categoryId,
    String categoryName
) {}
