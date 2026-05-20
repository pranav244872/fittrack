package com.pranav244872.fitness_tracker.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "banned_emails")
public class BannedEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime bannedAt;

    private String reason;

    public BannedEmail() {}
    public BannedEmail(String email, String reason) {
        this.email = email;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getBannedAt() { return bannedAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
