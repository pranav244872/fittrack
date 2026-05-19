package com.pranav244872.fitness_tracker.service;

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

    // Manual constructor injection
    public MeditationLogServiceImpl(MeditationLogRepository meditationLogRepository) {
        this.meditationLogRepository = meditationLogRepository;
    }

    @Override
    public MeditationLog logMeditation(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Meditation duration must be greater than zero minutes");
        }

        // Get the currently logged-in user
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MeditationLog log = new MeditationLog();
        log.setCompletionDate(LocalDateTime.now());
        log.setDurationMinutes(durationMinutes);
        log.setUser(currentUser); // Attach the user!

        return meditationLogRepository.save(log);
    }

    @Override
    public List<MeditationLog> getAllMeditationLogs() {
        return meditationLogRepository.findAll();
    }

    @Override
    public void deleteMeditationLog(Long id) {
        if (!meditationLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meditation log not found with id: " + id);
        }
        meditationLogRepository.deleteById(id);
    }
}
