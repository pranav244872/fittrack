package com.pranav244872.fitness_tracker.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "meditation_tracks")
public class MeditationTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String filename;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    private long fileSizeBytes;

    public MeditationTrack() {}
    public MeditationTrack(String displayName, String filename, long fileSizeBytes) {
        this.displayName = displayName;
        this.filename = filename;
        this.fileSizeBytes = fileSizeBytes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
}
