package com.example.courseservice;

import com.example.courseservice.domain.course.entity.CourseSession;
import com.example.courseservice.domain.course.repository.CourseSessionRepository;
import com.example.courseservice.domain.course.service.CourseSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseSessionServiceTest {

    @Mock
    private CourseSessionRepository courseSessionRepository;

    @InjectMocks
    private CourseSessionService courseSessionService;

    @Test
    public void testAddCourseSessionToCourse() {
        // given
        Long courseId = 1L;
        CourseSession courseSession = new CourseSession();

        // when
        when(courseSessionRepository.save(any(CourseSession.class))).thenReturn(courseSession);
        CourseSession result = courseSessionService.addCourseSessionToCourse(courseId, courseSession);

        // then
        assertNotNull(result);
        verify(courseSessionRepository).save(courseSession);
        assertEquals(courseId, result.getCourse().getId());
    }

    @Test
    public void tesUpdateCourseSession() {
        // given
        Long courseSessionId = 1L;
        CourseSession existingCourseSession = new CourseSession();
        existingCourseSession.setId(courseSessionId);
        existingCourseSession.setTitle("Original Title");

        CourseSession updatedCourseSession = new CourseSession();
        updatedCourseSession.setTitle("Updated Title");

        // when
        when(courseSessionRepository.findById(courseSessionId)).thenReturn(Optional.of(existingCourseSession));
        when(courseSessionRepository.save(any(CourseSession.class))).thenReturn(updatedCourseSession);
        CourseSession result = courseSessionService.updateCourseSession(1L, updatedCourseSession);

        // then
        assertNotNull(result);
        verify(courseSessionRepository).findById(courseSessionId);
        verify(courseSessionRepository).save(existingCourseSession);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    public void testGetCourseSessionById() {
        // given
        Long courseSessionId = 1L;
        CourseSession courseSession = new CourseSession();

        // when
        when(courseSessionRepository.findById(courseSessionId)).thenReturn(Optional.of(courseSession));
        Optional<CourseSession> result = courseSessionService.getCourseSessionById(courseSessionId);

        // then
        assertTrue(result.isPresent());
        assertSame(courseSession, result.get());
        verify(courseSessionRepository).findById(courseSessionId);
    }

    @Test
    public void testGetAllCourseSessionsByCourseId() {
        // given
        Long courseId = 1L;
        List<CourseSession> courseSessions = Arrays.asList(new CourseSession(), new CourseSession());

        // when
        when(courseSessionRepository.findByCourseId(courseId)).thenReturn(courseSessions);
        List<CourseSession> result = courseSessionService.getAllCourseSessionsByCourseId(courseId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseSessionRepository).findByCourseId(1L);
    }
}
