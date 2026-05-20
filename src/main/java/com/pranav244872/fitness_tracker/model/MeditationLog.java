package com.pranav244872.fitness_tracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MeditationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int durationMinutes;

    @Column(nullable = false)
    public LocalDateTime completionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Track that was played during this session (nullable for sessions without music)
    @Column(name = "track_id")
    private Long trackId;

    public MeditationLog() {}

    public MeditationLog(Long id, int durationMinutes) {
        this.id = id;
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) { this.completionDate = completionDate; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }
}
