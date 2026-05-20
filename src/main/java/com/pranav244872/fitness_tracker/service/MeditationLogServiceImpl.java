package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.MeditationLogResponse;
import com.pranav244872.fitness_tracker.model.MeditationLog;
import com.pranav244872.fitness_tracker.model.User;
import com.pranav244872.fitness_tracker.repository.MeditationLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeditationLogServiceImpl implements MeditationLogService {
    private final MeditationLogRepository meditationLogRepository;

    public MeditationLogServiceImpl(MeditationLogRepository meditationLogRepository) {
        this.meditationLogRepository = meditationLogRepository;
    }

    @Override
    public MeditationLogResponse logMeditation(int durationMinutes) {
        return logMeditation(durationMinutes, null);
    }

    @Override
    public MeditationLogResponse logMeditation(int durationMinutes, Long trackId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MeditationLog log = new MeditationLog();
        log.setDurationMinutes(durationMinutes);
        log.setCompletionDate(LocalDateTime.now());
        log.setUser(user);
        log.setTrackId(trackId);
        MeditationLog saved = meditationLogRepository.save(log);
        return new MeditationLogResponse(saved.getId(), saved.getDurationMinutes(), saved.getCompletionDate(), saved.getTrackId());
    }

    @Override
    public List<MeditationLogResponse> getAllMeditationLogs() {
        return meditationLogRepository.findAll().stream()
                .map(log -> new MeditationLogResponse(log.getId(), log.getDurationMinutes(), log.getCompletionDate(), log.getTrackId()))
                .toList();
    }

    @Override
    public List<MeditationLogResponse> getMeditationLogsByMonth(int year, int month, Long userId) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        return meditationLogRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
                .map(log -> new MeditationLogResponse(log.getId(), log.getDurationMinutes(), log.getCompletionDate(), log.getTrackId()))
                .toList();
    }
}
