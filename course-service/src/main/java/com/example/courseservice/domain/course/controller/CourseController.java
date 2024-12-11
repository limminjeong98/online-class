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
    ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return ResponseEntity.ok(course);
    }

    @GetMapping
    ResponseEntity<List<Course>> getAllCourses(@RequestParam(required = false) List<Long> courseIds) {
        List<Course> courses;
        if (courseIds == null || courseIds.isEmpty()) {
            courses = courseService.getAllCourses();
        } else {
            courses = courseService.getCourseByIds(courseIds);
        }
        return ResponseEntity.ok(courses);
    }

}
