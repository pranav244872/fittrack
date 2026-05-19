package com.pranav244872.fitness_tracker.dto;

public record WorkoutResponse(
    Long id,
    String name,
    int targetSets,
    int targetReps,
    int restBetweenSetsSeconds
) {}
