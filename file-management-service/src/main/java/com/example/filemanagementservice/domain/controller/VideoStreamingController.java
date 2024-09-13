package com.example.filemanagementservice.domain.controller;

import com.example.filemanagementservice.domain.entity.SessionFile;
import com.example.filemanagementservice.domain.service.SessionFileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/sessions/{sessionId}")
public class VideoStreamingController {

    private final SessionFileService sessionFileService;

    public VideoStreamingController(SessionFileService sessionFileService) {
        this.sessionFileService = sessionFileService;
    }

    @GetMapping("/streams")
    public ResponseEntity<?> streamVideo(HttpServletRequest request,
                                         @PathVariable Long sessionId) {
        Optional<SessionFile> fileOptional = sessionFileService.findTopBySessionIdOrderByFileIdDesc(sessionId);
        return fileOptional.map(file -> {
            try {

                Path filePath = Paths.get(file.getFilePath());
                log.info("filePath = {}", filePath);
                log.info("filePath.toUri() = {}", filePath.toUri());

                Resource video = new UrlResource(filePath.toUri());
                if (video.exists() || video.isReadable()) {

                    FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
                    long fileLength = fileChannel.size();

                    long start = 0;
                    long end = fileLength - 1;

                    String rangeHeader = request.getHeader("Range");

                    if (rangeHeader != null) {
                        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
                        if (ranges.length > 0) {
                            start = Long.parseLong(ranges[0]);
                        }
                        if (ranges.length > 1) {
                            end = Long.parseLong(ranges[1]);
                        }
                        if (start > fileLength - 1 || end > fileLength - 1) {
                            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
                        }
                    }

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
                    if (rangeHeader != null) {
                        httpHeaders.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);
                        httpHeaders.add(HttpHeaders.ACCEPT_RANGES, "bytes");
                        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(end - start + 1));
                    }

                    InputStreamResource resource = new InputStreamResource(Channels.newInputStream(fileChannel.position(start)));
                    log.info("resource = {}", resource);

                    return ResponseEntity.status(rangeHeader == null ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT)
                            .headers(httpHeaders)
                            .body(resource);

                } else {
                    throw new RuntimeException("Could not read file: " + file.getFileName());
                }
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
