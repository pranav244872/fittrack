package com.pranav244872.fitness_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranav244872.fitness_tracker.dto.AuthDTOs.LoginRequest;
import com.pranav244872.fitness_tracker.dto.AuthDTOs.RegisterRequest;
import com.pranav244872.fitness_tracker.dto.AuthDTOs.AuthResponse;
import com.pranav244872.fitness_tracker.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService service;

    public AuthController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request.username(), request.email(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.authenticate(request.username(), request.password()));
    }
}
