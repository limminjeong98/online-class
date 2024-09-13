package com.example.filemanagementservice.domain.controller;

import com.example.filemanagementservice.domain.entity.SessionFile;
import com.example.filemanagementservice.domain.service.FileStorageService;
import com.example.filemanagementservice.domain.service.SessionFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sessions/{sessionId}/files")
public class SessionFileController {

    private final SessionFileService sessionFileService;
    private final FileStorageService fileStorageService;

    public SessionFileController(SessionFileService sessionFileService, FileStorageService fileStorageService) {
        this.sessionFileService = sessionFileService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<SessionFile> uploadFile(@PathVariable Long sessionId, @RequestParam("file") MultipartFile file) {
        SessionFile sessionFile = fileStorageService.storeFile(file, sessionId);
        return ResponseEntity.ok(sessionFileService.saveSessionFile(sessionFile));
    }

    @GetMapping
    public ResponseEntity<SessionFile> getRecentFileBySessionId(@PathVariable Long sessionId) {
        return sessionFileService.findTopBySessionIdOrderByFileIdDesc(sessionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<SessionFile> getFileById(@PathVariable Long sessionId, @PathVariable Long fileId) {
        return sessionFileService.findSessionFileById(fileId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long sessionId, @PathVariable Long fileId) {
        sessionFileService.deleteSessionFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
