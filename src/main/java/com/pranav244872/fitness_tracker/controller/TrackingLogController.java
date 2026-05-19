package com.pranav244872.fitness_tracker.controller;

import com.pranav244872.fitness_tracker.dto.MeditationLogRequest;
import com.pranav244872.fitness_tracker.dto.WorkoutLogRequest;
import com.pranav244872.fitness_tracker.model.MeditationLog;
import com.pranav244872.fitness_tracker.model.WorkoutLog;
import com.pranav244872.fitness_tracker.service.MeditationLogService;
import com.pranav244872.fitness_tracker.service.WorkoutLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class TrackingLogController {

    private final WorkoutLogService workoutLogService;
    private final MeditationLogService meditationLogService;

    public TrackingLogController(WorkoutLogService workoutLogService, MeditationLogService meditationLogService) {
        this.workoutLogService = workoutLogService;
        this.meditationLogService = meditationLogService;
    }

    // Log a completed workout routine
    // Expects JSON body: { "categoryId": 1, "durationMinutes": 45 }
    @PostMapping("/workouts")
    public ResponseEntity<WorkoutLog> logWorkout(@RequestBody WorkoutLogRequest request) {
        WorkoutLog createdLog = workoutLogService.logWorkout(
            request.categoryId(), 
            request.durationMinutes()
        );
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutLog>> getAllWorkoutLogs() {
        return ResponseEntity.ok(workoutLogService.getAllWorkoutLogs());
    }

    // Log a meditation session
    // Expects JSON body: { "durationMinutes": 15 }
    @PostMapping("/meditation")
    public ResponseEntity<MeditationLog> logMeditation(@RequestBody MeditationLogRequest request) {
        MeditationLog createdLog = meditationLogService.logMeditation(request.durationMinutes());
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/meditation")
    public ResponseEntity<List<MeditationLog>> getAllMeditationLogs() {
        return ResponseEntity.ok(meditationLogService.getAllMeditationLogs());
    }
}
