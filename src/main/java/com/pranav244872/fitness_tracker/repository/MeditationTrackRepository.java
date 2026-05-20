package com.pranav244872.fitness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pranav244872.fitness_tracker.model.MeditationTrack;

@Repository
public interface MeditationTrackRepository extends JpaRepository<MeditationTrack, Long> {
    boolean existsByFilename(String filename);
}
