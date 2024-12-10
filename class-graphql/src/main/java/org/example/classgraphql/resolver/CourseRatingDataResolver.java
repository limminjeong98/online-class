package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.CourseRating;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.CourseService;
import org.example.classgraphql.service.DummyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CourseRatingDataResolver {

    private final DummyUserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseRatingDataResolver(DummyUserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @SchemaMapping(typeName = "CourseRating", field = "course")
    public Course getCourse(CourseRating courseRating) {
        return courseService.findCourseById(courseRating.getCourseId()).orElseThrow();
    }

    @SchemaMapping(typeName = " CourseRating", field = "user")
    public User getUser(CourseRating courseRating) {
        return userService.findById(courseRating.getUserId()).orElseThrow();
    }
}
