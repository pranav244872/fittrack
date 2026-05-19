package com.pranav244872.fitness_tracker.controller;

import com.pranav244872.fitness_tracker.dto.MeditationLogRequest;
import com.pranav244872.fitness_tracker.dto.MeditationLogResponse;
import com.pranav244872.fitness_tracker.dto.WorkoutLogRequest;
import com.pranav244872.fitness_tracker.dto.WorkoutLogResponse;
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

    @PostMapping("/workouts")
    public ResponseEntity<WorkoutLogResponse> logWorkout(@RequestBody WorkoutLogRequest request) {
        WorkoutLogResponse createdLog = workoutLogService.logWorkout(
            request.categoryId(),
            request.durationMinutes()
        );
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutLogResponse>> getAllWorkoutLogs() {
        return ResponseEntity.ok(workoutLogService.getAllWorkoutLogs());
    }

    @PostMapping("/meditation")
    public ResponseEntity<MeditationLogResponse> logMeditation(@RequestBody MeditationLogRequest request) {
        MeditationLogResponse createdLog = meditationLogService.logMeditation(request.durationMinutes());
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/meditation")
    public ResponseEntity<List<MeditationLogResponse>> getAllMeditationLogs() {
        return ResponseEntity.ok(meditationLogService.getAllMeditationLogs());
    }
}
