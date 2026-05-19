package com.pranav244872.fitness_tracker.controller;

import com.pranav244872.fitness_tracker.dto.WorkoutResponse;
import com.pranav244872.fitness_tracker.model.Workout;
import com.pranav244872.fitness_tracker.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/categories/{categoryId}/workouts")
    public ResponseEntity<WorkoutResponse> createWorkout(@PathVariable Long categoryId, @RequestBody Workout workout) {
        WorkoutResponse created = workoutService.createWorkout(categoryId, workout);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{categoryId}/workouts")
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(workoutService.getWorkoutsByCategoryId(categoryId));
    }

    @GetMapping("/workouts/{id}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutById(id));
    }

    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
