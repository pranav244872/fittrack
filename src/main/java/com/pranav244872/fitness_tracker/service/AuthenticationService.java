package com.pranav244872.fitness_tracker.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pranav244872.fitness_tracker.dto.AuthDTOs.AuthResponse;
import com.pranav244872.fitness_tracker.model.User;
import com.pranav244872.fitness_tracker.repository.UserRepository;
import com.pranav244872.fitness_tracker.security.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

    public AuthResponse register(String username, String email, String password) {
        log.info("Processing registration for username: {}", username);
        
        if (repository.existsByUsername(username)) {
            log.warn("Registration failed: Username {} is already taken", username);
            throw new IllegalArgumentException("Username is already taken");
        }
        
        if (repository.existsByEmail(email)) {
            log.warn("Registration failed: Email {} is already registered", email);
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);

        repository.save(user);
        log.info("User {} successfully registered and saved to database", username);

        String jwtToken = jwtService.generateToken(user);
        log.info("Generated JWT token for new user: {}", username);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(String username, String password) {
        log.info("Attempting authentication for username: {}", username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info("Authentication successful for username: {}", username);
        } catch (Exception e) {
            log.warn("Authentication failed for username: {} - Reason: {}", username, e.getMessage());
            throw e;
        }

        User user = repository.findByUsername(username).orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        log.info("Generated JWT token for authenticated user: {}", username);
        return new AuthResponse(jwtToken);
    }
}
