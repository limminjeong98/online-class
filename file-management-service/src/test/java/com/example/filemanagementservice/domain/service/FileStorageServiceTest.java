package com.example.filemanagementservice.domain.service;

import com.example.filemanagementservice.domain.entity.SessionFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    @Test
    void testStoreFile_Success() throws IOException {
        // given
        Long sessionId = 1L;
        String originalFileName = "test.mp4";
        MockMultipartFile mockFile = new MockMultipartFile("file", originalFileName,
                "video/mp4", new ByteArrayInputStream("some data".getBytes()));
        String uploadDir = "./uploads";
        ReflectionTestUtils.setField(fileStorageService, "uploadDir", uploadDir);

        // when
        SessionFile result = fileStorageService.storeFile(mockFile, sessionId);

        // then
        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
        assertTrue(result.getFileName().contains("_" + originalFileName));
        assertTrue(result.getFilePath().contains(result.getFileName()));
        assertEquals("mp4", result.getFileType());
    }

    @Test
    void testStoreFile_ThrowsException_ForInvalidPath() throws IOException {
        // given
        Long sessionId = 1L;
        String originalFileName = "../test.mp4";
        MockMultipartFile mockFile = new MockMultipartFile("file", originalFileName,
                "video/mp4", new ByteArrayInputStream("some data".getBytes()));

        // when & then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(mockFile, sessionId);
        });
        assertTrue(exception.getMessage().contains("Filename contains invalid path sequence"));
    }
}