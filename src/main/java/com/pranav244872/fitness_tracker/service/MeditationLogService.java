package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.model.MeditationLog;
import java.util.List;

public interface MeditationLogService {
    MeditationLog logMeditation(int durationMinutes);
    List<MeditationLog> getAllMeditationLogs();
    void deleteMeditationLog(Long id);
}
