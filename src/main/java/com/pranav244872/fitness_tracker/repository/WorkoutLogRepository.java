package com.pranav244872.fitness_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pranav244872.fitness_tracker.model.WorkoutLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    @Query("SELECT w FROM WorkoutLog w WHERE w.category.user.id = :userId AND w.completionDate >= :startDate AND w.completionDate < :endDate")
    List<WorkoutLog> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByCompletionDateAfter(LocalDateTime date);

    @Query("SELECT COUNT(DISTINCT w.category.user.id) FROM WorkoutLog w WHERE w.completionDate >= :date")
    long countDistinctUsersAfter(@Param("date") LocalDateTime date);
}
