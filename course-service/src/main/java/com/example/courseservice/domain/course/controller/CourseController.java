package com.example.courseservice.domain.course.controller;

import com.example.courseservice.domain.course.entity.Course;
import com.example.courseservice.domain.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    ResponseEntity<Course> createCourse(@RequestBody Course request) {
        Course course = courseService.saveCourse(request);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}")
    ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @RequestBody Course request) {
        Course course = courseService.updateCourse(courseId, request);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{courseId}")
    ResponseEntity<Course> getCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return ResponseEntity.ok(course);
    }

    @GetMapping
    ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

}
