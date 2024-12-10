package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.Enrollment;
import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.CourseService;
import org.example.classgraphql.service.EnrollmentService;
import org.example.classgraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EnrollmentDataResolver {

    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentDataResolver(UserService userService, CourseService courseService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @SchemaMapping(typeName = "Enrollment", field = "user")
    public User getUser(Enrollment enrollment) {
        return userService.findById(enrollment.getUserId()).orElseThrow();
    }

    @SchemaMapping(typeName = "Enrollment", field = "course")
    public Course getCourse(Enrollment enrollment) {
        return courseService.findCourseById(enrollment.getCourseId()).orElseThrow();
    }

    @SchemaMapping(typeName = "Enrollment", field = "payment")
    public Payment getPayment(Enrollment enrollment) {
        return enrollmentService.findPaymentById(enrollment.getPaymentId());
    }

}
