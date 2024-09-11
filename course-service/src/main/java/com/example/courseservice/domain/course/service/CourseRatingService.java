package com.example.courseservice.domain.course.service;

import com.example.courseservice.domain.course.entity.Course;
import com.example.courseservice.domain.course.entity.CourseRating;
import com.example.courseservice.domain.course.repository.CourseRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseRatingService {

    private final CourseRatingRepository courseRatingRepository;

    @Autowired
    public CourseRatingService(CourseRatingRepository courseRatingRepository) {
        this.courseRatingRepository = courseRatingRepository;
    }

    public CourseRating addCourseRatingToCourse(Long courseId, CourseRating courseRating) {
        courseRating.setCourse(new Course(courseId));
        return courseRatingRepository.save(courseRating);
    }

    public CourseRating updateCourseRating(Long courseRatingId, CourseRating courseRatingDetails) {
        CourseRating courseRating = courseRatingRepository.findById(courseRatingId)
                .orElseThrow(() -> new RuntimeException("rating not found"));

        courseRating.setRating(courseRatingDetails.getRating());
        courseRating.setComment(courseRatingDetails.getComment());

        return courseRatingRepository.save(courseRating);
    }

    public void deleteRating(Long courseRatingId) {
        courseRatingRepository.deleteById(courseRatingId);
    }

    public List<CourseRating> getAllRatingsByCourseId(Long courseId) {
        return courseRatingRepository.findByCourseId(courseId);
    }

    public Optional<CourseRating> getCourseRatingById(Long courseRatingId) {
        return courseRatingRepository.findById(courseRatingId);
    }
}
