package com.pranav244872.fitness_tracker.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class FitnessController {
    @GetMapping("/register")
    public String registerUser() {
        return "Hello User!";
    }
}
