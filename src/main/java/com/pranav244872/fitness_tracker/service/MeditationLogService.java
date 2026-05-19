package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.MeditationLogResponse;
import com.pranav244872.fitness_tracker.model.MeditationLog;
import java.util.List;

public interface MeditationLogService {
    MeditationLogResponse logMeditation(int durationMinutes);
    List<MeditationLogResponse> getAllMeditationLogs();
    void deleteMeditationLog(Long id);
}
