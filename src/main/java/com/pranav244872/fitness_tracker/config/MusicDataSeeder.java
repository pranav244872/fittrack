package com.pranav244872.fitness_tracker.config;

import com.pranav244872.fitness_tracker.model.MeditationTrack;
import com.pranav244872.fitness_tracker.repository.MeditationTrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MusicDataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MusicDataSeeder.class);

    private final MeditationTrackRepository trackRepository;

    @Value("${music.storage.path:./music}")
    private String musicStoragePath;

    public MusicDataSeeder(MeditationTrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public void run(String... args) {
        seedTrack("music1.mp3", "Ethereal Ambient");
        seedTrack("music2.mp3", "Deep Meditation");
        seedTrack("music3.mp3", "Peaceful Flow");
        seedTrack("music4.mp3", "Rain Atmosphere");
    }

    private void seedTrack(String filename, String displayName) {
        if (trackRepository.existsByFilename(filename)) {
            return;
        }
        File file = new File(musicStoragePath, filename);
        if (!file.exists()) {
            log.warn("Seed music file not found: {}", file.getAbsolutePath());
            return;
        }
        MeditationTrack track = new MeditationTrack(displayName, filename, file.length());
        trackRepository.save(track);
        log.info("Seeded meditation track: '{}' ({})", displayName, filename);
    }
}
