package com.example.filemanagementservice.domain.service;

import com.example.filemanagementservice.domain.entity.SessionFile;
import com.example.filemanagementservice.domain.repository.SessionFileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionFileService {
    private final SessionFileRepository sessionFileRepository;

    public SessionFileService(SessionFileRepository sessionFileRepository) {
        this.sessionFileRepository = sessionFileRepository;
    }

    public SessionFile saveSessionFile(SessionFile sessionFile) {
        return sessionFileRepository.save(sessionFile);
    }

    public Optional<SessionFile> findTopBySessionIdOrderByFileIdDesc(Long sessionId) {
        return sessionFileRepository.findTopBySessionIdOrderByFileIdDesc(sessionId);
    }

    public Optional<SessionFile> findSessionFileById(Long fileId) {
        return sessionFileRepository.findById(fileId);
    }

    public void deleteSessionFile(Long fileId) {
        sessionFileRepository.deleteById(fileId);
    }

}
