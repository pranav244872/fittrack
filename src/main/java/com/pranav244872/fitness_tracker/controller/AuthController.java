package com.pranav244872.fitness_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.pranav244872.fitness_tracker.model.User;
import com.pranav244872.fitness_tracker.dto.UserProfileResponse;
import com.pranav244872.fitness_tracker.dto.AuthDTOs.LoginRequest;
import com.pranav244872.fitness_tracker.dto.AuthDTOs.RegisterRequest;
import com.pranav244872.fitness_tracker.dto.AuthDTOs.AuthResponse;
import com.pranav244872.fitness_tracker.service.AuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService service;

    public AuthController(AuthenticationService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        String createdAtStr = user.getCreatedAt() != null ? user.getCreatedAt().toString() : "";
        return ResponseEntity.ok(new UserProfileResponse(user.getUsername(), user.getEmail(), createdAtStr));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for username: {}, email: {}", request.username(), request.email());
        return ResponseEntity.ok(service.register(request.username(), request.email(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request) {
        log.info("Received login request for username: {}", request.username());
        return ResponseEntity.ok(service.authenticate(request.username(), request.password()));
    }
}
