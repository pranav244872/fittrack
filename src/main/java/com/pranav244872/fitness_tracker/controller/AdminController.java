package com.pranav244872.fitness_tracker.controller;

import com.pranav244872.fitness_tracker.config.InMemoryLogAppender;
import com.pranav244872.fitness_tracker.model.*;
import com.pranav244872.fitness_tracker.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserRepository userRepository;
    private final BannedEmailRepository bannedEmailRepository;
    private final MeditationTrackRepository meditationTrackRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final MeditationLogRepository meditationLogRepository;
    private final CategoryRepository categoryRepository;

    @Value("${music.storage.path:./music}")
    private String musicStoragePath;

    public AdminController(UserRepository userRepository,
                           BannedEmailRepository bannedEmailRepository,
                           MeditationTrackRepository meditationTrackRepository,
                           WorkoutLogRepository workoutLogRepository,
                           MeditationLogRepository meditationLogRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.bannedEmailRepository = bannedEmailRepository;
        this.meditationTrackRepository = meditationTrackRepository;
        this.workoutLogRepository = workoutLogRepository;
        this.meditationLogRepository = meditationLogRepository;
        this.categoryRepository = categoryRepository;
    }

    // ─── Users ───────────────────────────────────────────
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = userRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", user.getId());
                    map.put("username", user.getUsername());
                    map.put("email", user.getEmail());
                    map.put("role", user.getRole().name());
                    map.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
                    return map;
                }).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        return ResponseEntity.ok(Map.of("count", userRepository.count()));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        var userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        log.warn("ADMIN: Deleting user {} (id: {})", user.getUsername(), id);
        // Delete associated data
        categoryRepository.deleteAll(categoryRepository.findByUser(user));
        userRepository.delete(user);
        return ResponseEntity.ok(Map.of("message", "User " + user.getUsername() + " deleted"));
    }

    // ─── Banned Emails ───────────────────────────────────
    @GetMapping("/banned-emails")
    public ResponseEntity<List<BannedEmail>> getBannedEmails() {
        return ResponseEntity.ok(bannedEmailRepository.findAll());
    }

    @PostMapping("/banned-emails")
    public ResponseEntity<BannedEmail> banEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email").toLowerCase();
        String reason = body.getOrDefault("reason", "Banned by admin");
        if (bannedEmailRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().build();
        }
        BannedEmail banned = new BannedEmail(email, reason);
        log.warn("ADMIN: Banning email: {} reason: {}", email, reason);
        return ResponseEntity.ok(bannedEmailRepository.save(banned));
    }

    @DeleteMapping("/banned-emails/{id}")
    public ResponseEntity<Map<String, String>> unbanEmail(@PathVariable Long id) {
        bannedEmailRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Email unbanned"));
    }

    // ─── Logs ────────────────────────────────────────────
    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs(@RequestParam(defaultValue = "200") int lines) {
        return ResponseEntity.ok(InMemoryLogAppender.getLastNLogs(lines));
    }

    // ─── System Health ───────────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new LinkedHashMap<>();

        // Disk space
        File root = new File("/");
        health.put("diskTotalGB", root.getTotalSpace() / (1024.0 * 1024 * 1024));
        health.put("diskFreeGB", root.getFreeSpace() / (1024.0 * 1024 * 1024));
        health.put("diskUsedGB", (root.getTotalSpace() - root.getFreeSpace()) / (1024.0 * 1024 * 1024));
        health.put("diskUsagePercent", ((root.getTotalSpace() - root.getFreeSpace()) * 100.0) / root.getTotalSpace());

        // JVM Memory
        Runtime rt = Runtime.getRuntime();
        health.put("jvmMaxMemoryMB", rt.maxMemory() / (1024.0 * 1024));
        health.put("jvmTotalMemoryMB", rt.totalMemory() / (1024.0 * 1024));
        health.put("jvmFreeMemoryMB", rt.freeMemory() / (1024.0 * 1024));
        health.put("jvmUsedMemoryMB", (rt.totalMemory() - rt.freeMemory()) / (1024.0 * 1024));

        // Database (simple check)
        try {
            long userCount = userRepository.count();
            health.put("databaseStatus", "HEALTHY");
            health.put("databaseUserCount", userCount);
        } catch (Exception e) {
            health.put("databaseStatus", "UNHEALTHY: " + e.getMessage());
        }

        // Music storage
        File musicDir = new File(musicStoragePath);
        health.put("musicStorageExists", musicDir.exists());
        if (musicDir.exists()) {
            long totalMusicSize = 0;
            File[] files = musicDir.listFiles();
            if (files != null) {
                for (File f : files) totalMusicSize += f.length();
            }
            health.put("musicFilesCount", files != null ? files.length : 0);
            health.put("musicStorageMB", totalMusicSize / (1024.0 * 1024));
        }

        health.put("uptimeSeconds", java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime() / 1000);

        return ResponseEntity.ok(health);
    }

    // ─── Analytics ───────────────────────────────────────
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.toLocalDate().minusDays(7).atStartOfDay();

        analytics.put("totalUsers", userRepository.count());
        analytics.put("workoutsToday", workoutLogRepository.countByCompletionDateAfter(todayStart));
        analytics.put("workoutsThisWeek", workoutLogRepository.countByCompletionDateAfter(weekStart));
        analytics.put("meditationsToday", meditationLogRepository.countByCompletionDateAfter(todayStart));
        analytics.put("meditationsThisWeek", meditationLogRepository.countByCompletionDateAfter(weekStart));

        // DAU - distinct users who logged anything today
        long dauWorkouts = workoutLogRepository.countDistinctUsersAfter(todayStart);
        long dauMeditation = meditationLogRepository.countDistinctUsersAfter(todayStart);
        analytics.put("dailyActiveUsers", Math.max(dauWorkouts, dauMeditation));

        // Most popular track
        List<Object[]> popularTrack = meditationLogRepository.findMostPopularTrack();
        if (!popularTrack.isEmpty() && popularTrack.get(0)[0] != null) {
            Object[] row = popularTrack.get(0);
            Long trackId = ((Number) row[0]).longValue();
            Long count = ((Number) row[1]).longValue();
            String trackName = meditationTrackRepository.findById(trackId)
                    .map(MeditationTrack::getDisplayName).orElse("Unknown");
            analytics.put("popularTrackName", trackName);
            analytics.put("popularTrackPlays", count);
        } else {
            analytics.put("popularTrackName", "None yet");
            analytics.put("popularTrackPlays", 0);
        }

        return ResponseEntity.ok(analytics);
    }

    // ─── Music Management ────────────────────────────────
    @GetMapping("/music")
    public ResponseEntity<List<MeditationTrack>> getAllTracks() {
        return ResponseEntity.ok(meditationTrackRepository.findAll());
    }

    @PostMapping("/music")
    public ResponseEntity<MeditationTrack> uploadTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String displayName) throws IOException {
        Path musicDir = Paths.get(musicStoragePath);
        if (!Files.exists(musicDir)) {
            Files.createDirectories(musicDir);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = musicDir.resolve(filename);
        Files.write(filePath, file.getBytes());

        MeditationTrack track = new MeditationTrack(displayName, filename, file.getSize());
        MeditationTrack saved = meditationTrackRepository.save(track);
        log.info("ADMIN: Uploaded music track '{}' as {}", displayName, filename);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/music/{id}")
    public ResponseEntity<Map<String, String>> deleteTrack(@PathVariable Long id) {
        var trackOpt = meditationTrackRepository.findById(id);
        if (trackOpt.isEmpty()) return ResponseEntity.notFound().build();

        MeditationTrack track = trackOpt.get();
        // Delete file
        try {
            Files.deleteIfExists(Paths.get(musicStoragePath, track.getFilename()));
        } catch (IOException e) {
            log.error("Failed to delete music file: {}", track.getFilename(), e);
        }
        meditationTrackRepository.delete(track);
        log.warn("ADMIN: Deleted music track '{}' (id: {})", track.getDisplayName(), id);
        return ResponseEntity.ok(Map.of("message", "Track deleted"));
    }
}
