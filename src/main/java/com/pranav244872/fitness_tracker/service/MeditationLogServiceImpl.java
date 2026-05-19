package com.pranav244872.fitness_tracker.service;

import com.pranav244872.fitness_tracker.dto.MeditationLogResponse;
import com.pranav244872.fitness_tracker.exception.ResourceNotFoundException;
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
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Meditation duration must be greater than zero minutes");
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MeditationLog log = new MeditationLog();
        log.setCompletionDate(LocalDateTime.now());
        log.setDurationMinutes(durationMinutes);
        log.setUser(currentUser);

        MeditationLog saved = meditationLogRepository.save(log);
        return toResponse(saved);
    }

    @Override
    public List<MeditationLogResponse> getAllMeditationLogs() {
        return meditationLogRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public void deleteMeditationLog(Long id) {
        if (!meditationLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meditation log not found with id: " + id);
        }
        meditationLogRepository.deleteById(id);
    }

    private MeditationLogResponse toResponse(MeditationLog log) {
        return new MeditationLogResponse(log.getId(), log.getDurationMinutes(), log.getCompletionDate());
    }
}
