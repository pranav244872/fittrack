
package com.pranav244872.fitness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pranav244872.fitness_tracker.model.MeditationLog;

@Repository
public interface MeditationLogRepository extends JpaRepository<MeditationLog, Long> {

}
