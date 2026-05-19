package com.pranav244872.fitness_tracker.dto;

import java.time.LocalDateTime;

public record MeditationLogResponse(
    Long id,
    int durationMinutes,
    LocalDateTime completionDate
) {}
