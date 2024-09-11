package com.example.courseservice.domain.course.controller;

import com.example.courseservice.domain.course.entity.CourseSession;
import com.example.courseservice.domain.course.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/sessions")
public class CourseSessionController {

    private final CourseSessionService courseSessionService;

    @Autowired
    public CourseSessionController(CourseSessionService courseSessionService) {
        this.courseSessionService = courseSessionService;
    }

    @PostMapping
    public ResponseEntity<CourseSession> createCourseSession(@PathVariable Long courseId, @RequestBody CourseSession request) {
        CourseSession courseSession = courseSessionService.addCourseSessionToCourse(courseId, request);
        return ResponseEntity.ok(courseSession);
    }

    @PutMapping("/{courseSessionId}")
    public ResponseEntity<CourseSession> updateCourseSession(@PathVariable Long courseId, @PathVariable Long courseSessionId, @RequestBody CourseSession request) {
        CourseSession courseSession = courseSessionService.updateCourseSession(courseId, request);
        return ResponseEntity.ok(courseSession);
    }

    @GetMapping("/{courseSessionId}")
    public ResponseEntity<CourseSession> getCourseSessionById(@PathVariable Long courseId, @PathVariable Long courseSessionId) {
        CourseSession courseSession = courseSessionService.getCourseSessionById(courseSessionId)
                .orElseThrow(() -> new RuntimeException("Course Session not found"));
        return ResponseEntity.ok(courseSession);
    }

    @GetMapping
    public ResponseEntity<List<CourseSession>> getCourseSessionsByCourseId(@PathVariable Long courseId) {
        List<CourseSession> courseSessions = courseSessionService.getAllCourseSessionsByCourseId(courseId);
        return ResponseEntity.ok(courseSessions);
    }
}
