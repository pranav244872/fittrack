package com.pranav244872.fitness_tracker.dto;

import java.util.List;

public record CategoryResponse(
    Long id,
    String name,
    List<WorkoutResponse> workouts
) {}
