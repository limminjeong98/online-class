package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.CourseSession;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.CourseService;
import org.example.classgraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CourseDataResolver {

    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseDataResolver(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @SchemaMapping(typeName = "Course", field = "instructor")
    public User getInstructor(Course course) {
        return userService.findById(course.getInstructorId()).orElseThrow();
    }

    @SchemaMapping(typeName = "Course", field = "courseSessions")
    public List<CourseSession> getSessions(Course course) {
        return courseService.findAllSessionsByCourseId(course.getId());
    }
}
