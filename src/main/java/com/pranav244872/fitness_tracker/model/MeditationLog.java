package com.pranav244872.fitness_tracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MeditationLog {
	///////////////////////////////////////////////////////////////////////////
	/// Fields
	///////////////////////////////////////////////////////////////////////////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    public LocalDateTime completionDate;

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

	///////////////////////////////////////////////////////////////////////////
	/// Constructors
	///////////////////////////////////////////////////////////////////////////
    
    public MeditationLog() {
    }

    public MeditationLog(Long id, int durationMinutes) {
        this.id = id;
        this.durationMinutes = durationMinutes;
    }

	///////////////////////////////////////////////////////////////////////////
	/// Getters and Setters
	///////////////////////////////////////////////////////////////////////////
    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
