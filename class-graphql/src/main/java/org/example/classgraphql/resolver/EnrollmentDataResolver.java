package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.Enrollment;
import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.DummyCourseService;
import org.example.classgraphql.service.DummyEnrollmentService;
import org.example.classgraphql.service.DummyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EnrollmentDataResolver {

    private final DummyUserService userService;
    private final DummyCourseService courseService;
    private final DummyEnrollmentService enrollmentService;

    @Autowired
    public EnrollmentDataResolver(DummyUserService userService, DummyCourseService courseService, DummyEnrollmentService enrollmentService) {
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
