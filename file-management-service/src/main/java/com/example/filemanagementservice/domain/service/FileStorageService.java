package com.example.filemanagementservice.domain.service;

import com.example.filemanagementservice.domain.entity.SessionFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileStorageService() {
    }

    /**
     * 파일을 서버에 저장하고 파일 정보를 반환합니다.
     *
     * @param file      MultipartFile
     * @param sessionId 세션 ID
     * @return 저장된 파일의 메타데이터
     */
    public SessionFile storeFile(MultipartFile file, Long sessionId) {

        log.info("file.getOriginalFilename() = {}", file.getOriginalFilename());
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = System.currentTimeMillis() + "_" + originalFileName;

        log.info("originalFileName = {}", originalFileName);
        log.info("fileName = {}", fileName);

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, REPLACE_EXISTING);

            log.info("targetLocation = {}", targetLocation);
            log.info("targetLocation.getParent() = {}", targetLocation.getParent());

            return new SessionFile(sessionId, fileName, "mp4", targetLocation.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }
    }
}
