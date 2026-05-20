package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.MeditationLogResponse;
import java.util.List;

public interface MeditationLogService {
    MeditationLogResponse logMeditation(int durationMinutes);
    MeditationLogResponse logMeditation(int durationMinutes, Long trackId);
    List<MeditationLogResponse> getAllMeditationLogs();
    List<MeditationLogResponse> getMeditationLogsByMonth(int year, int month, Long userId);
}
