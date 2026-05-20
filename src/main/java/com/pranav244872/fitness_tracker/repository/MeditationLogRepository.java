package com.pranav244872.fitness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pranav244872.fitness_tracker.model.MeditationLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeditationLogRepository extends JpaRepository<MeditationLog, Long> {
    @Query("SELECT m FROM MeditationLog m WHERE m.user.id = :userId AND m.completionDate >= :startDate AND m.completionDate < :endDate")
    List<MeditationLog> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByCompletionDateAfter(LocalDateTime date);

    @Query("SELECT COUNT(DISTINCT m.user.id) FROM MeditationLog m WHERE m.completionDate >= :date")
    long countDistinctUsersAfter(@Param("date") LocalDateTime date);

    @Query("SELECT m.trackId, COUNT(m) FROM MeditationLog m WHERE m.trackId IS NOT NULL GROUP BY m.trackId ORDER BY COUNT(m) DESC")
    List<Object[]> findMostPopularTrack();
}
