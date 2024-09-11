package com.example.courseservice.domain.course.controller;

import com.example.courseservice.domain.course.entity.CourseRating;
import com.example.courseservice.domain.course.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/ratings")
public class CourseRatingController {

    private final CourseRatingService courseRatingService;

    @Autowired
    public CourseRatingController(CourseRatingService courseRatingService) {
        this.courseRatingService = courseRatingService;
    }

    @PostMapping
    public ResponseEntity<CourseRating> createCourseRating(@PathVariable("courseId") Long courseId, @RequestBody CourseRating request) {
        CourseRating courseRating = courseRatingService.addCourseRatingToCourse(courseId, request);
        return ResponseEntity.ok(courseRating);
    }

    @PutMapping("/{courseRatingId}")
    public ResponseEntity<CourseRating> updateCourseRating(@PathVariable("courseId") Long courseId, @PathVariable("courseRatingId") Long courseRatingId, @RequestBody CourseRating request) {
        CourseRating courseRating = courseRatingService.updateCourseRating(courseRatingId, request);
        return ResponseEntity.ok(courseRating);
    }

    @GetMapping
    public ResponseEntity<List<CourseRating>> getCourseRatingsByCourseId(@PathVariable("courseId") Long courseId) {
        List<CourseRating> courseRatings = courseRatingService.getAllRatingsByCourseId(courseId);
        return ResponseEntity.ok(courseRatings);
    }

    @DeleteMapping("/{courseRatingId}")
    public ResponseEntity<Void> deleteCourseRating(@PathVariable("courseId") Long courseId, @PathVariable("courseRatingId") Long courseRatingId) {
        courseRatingService.deleteRating(courseRatingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseRatingId}")
    public ResponseEntity<CourseRating> getCourseRating(@PathVariable("courseId") Long courseId, @PathVariable("courseRatingId") Long courseRatingId) {
        CourseRating courseRating = courseRatingService.getCourseRatingById(courseRatingId)
                .orElseThrow(() -> new RuntimeException("Course rating not found"));
        return ResponseEntity.ok(courseRating);
    }
}
