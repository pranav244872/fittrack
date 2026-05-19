package com.pranav244872.fitness_tracker.dto;

public class AuthDTOs {
    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(String username, String email, String password) {}
    public record AuthResponse(String token) {}
}
