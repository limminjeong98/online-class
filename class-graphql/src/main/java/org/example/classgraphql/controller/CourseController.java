package org.example.classgraphql.controller;

import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.CourseRating;
import org.example.classgraphql.model.CourseSession;
import org.example.classgraphql.service.DummyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CourseController {

    private DummyCourseService courseService;

    @Autowired
    public CourseController(DummyCourseService courseService) {
        this.courseService = courseService;
    }

    @QueryMapping
    public List<Course> listCourses() {
        // throw new NotImplementedException();
        return courseService.findAllCourses();
    }

    @QueryMapping
    public Course getCourse(@Argument Long userId, @Argument Long courseId) {
        // throw new NotImplementedException();
        return courseService.findCourseById(courseId).orElseThrow(() -> new RuntimeException("Course Not Found"));
    }

    @QueryMapping
    public List<CourseSession> listCourseSessions(Long courseId) {
        // throw new NotImplementedException();
        return courseService.findAllSessionsByCourseId(courseId);
    }

    @MutationMapping
    public Course createCourse(@Argument String title, @Argument String description, @Argument Long instructorId) {
        // throw new NotImplementedException();
        return courseService.createCourse(title, description, instructorId);
    }

    @MutationMapping
    public Course updateCourse(@Argument Long courseId, @Argument String title, @Argument String description) {
        // throw new NotImplementedException();
        return courseService.updateCourse(courseId, title, description);
    }

    @MutationMapping
    public CourseSession addCourseSession(@Argument Long courseId, @Argument String title) {
        // throw new NotImplementedException();
        return courseService.addSessionToCourse(courseId, title);
    }

    @MutationMapping
    public CourseRating rateCourse(@Argument Long userId, @Argument Long courseId, @Argument Integer rating, @Argument String comment) {
        // throw new NotImplementedException();
        return courseService.addRatingToCourse(userId, courseId, rating, comment);
    }
}
