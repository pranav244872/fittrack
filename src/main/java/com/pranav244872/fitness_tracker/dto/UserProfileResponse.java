package com.pranav244872.fitness_tracker.dto;

public record UserProfileResponse(
    String username,
    String email,
    String createdAt
) {}
