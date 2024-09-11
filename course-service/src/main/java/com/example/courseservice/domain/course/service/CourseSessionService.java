package com.example.courseservice.domain.course.service;

import com.example.courseservice.domain.course.entity.Course;
import com.example.courseservice.domain.course.entity.CourseSession;
import com.example.courseservice.domain.course.repository.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;

    @Autowired
    public CourseSessionService(CourseSessionRepository courseSessionRepository) {
        this.courseSessionRepository = courseSessionRepository;
    }

    public CourseSession addCourseSessionToCourse(Long courseId, CourseSession courseSession) {
        courseSession.setCourse(new Course(courseId));
        return courseSessionRepository.save(courseSession);
    }

    public CourseSession updateCourseSession(Long courseSessionId, CourseSession sessionDetails) {
        CourseSession courseSession = courseSessionRepository.findById(courseSessionId)
                .orElseThrow(() -> new RuntimeException("session not found"));

        courseSession.setTitle(sessionDetails.getTitle());

        return courseSessionRepository.save(courseSession);
    }

    public Optional<CourseSession> getCourseSessionById(Long courseSessionId) {
        return courseSessionRepository.findById(courseSessionId);
    }

    public List<CourseSession> getAllCourseSessionsByCourseId(Long courseId) {
        return courseSessionRepository.findByCourseId(courseId);
    }
}
