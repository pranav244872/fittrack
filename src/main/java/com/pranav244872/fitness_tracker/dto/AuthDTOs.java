package com.pranav244872.fitness_tracker.dto;

public class AuthDTOs {
    public record AuthRequest(String username, String password) {}
    public record AuthResponse(String token) {}
}
