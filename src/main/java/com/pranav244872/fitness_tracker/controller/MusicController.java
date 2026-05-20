package com.pranav244872.fitness_tracker.controller;

import com.pranav244872.fitness_tracker.model.MeditationTrack;
import com.pranav244872.fitness_tracker.repository.MeditationTrackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
public class MusicController {
    private final MeditationTrackRepository trackRepository;

    @Value("${music.storage.path:./music}")
    private String musicStoragePath;

    public MusicController(MeditationTrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listTracks() {
        List<Map<String, Object>> tracks = trackRepository.findAll().stream()
                .map(t -> {
                    Map<String, Object> map = new java.util.LinkedHashMap<>();
                    map.put("id", t.getId());
                    map.put("displayName", t.getDisplayName());
                    map.put("fileSizeBytes", t.getFileSizeBytes());
                    map.put("uploadedAt", t.getUploadedAt() != null ? t.getUploadedAt().toString() : "");
                    return map;
                }).toList();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamTrack(@PathVariable Long id) {
        var trackOpt = trackRepository.findById(id);
        if (trackOpt.isEmpty()) return ResponseEntity.notFound().build();

        MeditationTrack track = trackOpt.get();
        File file = new File(musicStoragePath, track.getFilename());
        if (!file.exists()) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + track.getDisplayName() + ".mp3\"")
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadTrack(@PathVariable Long id) {
        var trackOpt = trackRepository.findById(id);
        if (trackOpt.isEmpty()) return ResponseEntity.notFound().build();

        MeditationTrack track = trackOpt.get();
        File file = new File(musicStoragePath, track.getFilename());
        if (!file.exists()) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + track.getDisplayName() + ".mp3\"")
                .contentLength(file.length())
                .body(resource);
    }
}
