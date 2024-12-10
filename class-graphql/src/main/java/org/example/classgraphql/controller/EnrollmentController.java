package org.example.classgraphql.controller;

import org.example.classgraphql.model.Enrollment;
import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.PlanSubscription;
import org.example.classgraphql.service.EnrollmentService;
import org.example.classgraphql.service.dummy.DummyPlaybackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @QueryMapping
    public boolean checkCourseAccess(@Argument Long userId, @Argument Long courseId) {
        // throw new NotImplementedException();
        // 특정 사용자가 구독 또는 개별 권한이 있는 경우 허용
        return enrollmentService.checkSubscriptionAccess(userId) || enrollmentService.checkCourseAccess(userId, courseId);
    }

    @QueryMapping
    public List<PlanSubscription> getUserPlanSubscriptions(@Argument Long userId) {
        // throw new NotImplementedException();
        return enrollmentService.getSubscriptionsByUserId(userId);
    }

    @QueryMapping
    public List<Enrollment> getUserEnrollments(@Argument Long userId) {
        // throw new NotImplementedException();
        return enrollmentService.getEnrollmentsByUserId(userId);
    }

    @MutationMapping
    public Payment purchaseCourse(@Argument Long userId, @Argument Long courseId, @Argument Float amount, @Argument String paymentMethod) {
        // throw new NotImplementedException();
        return enrollmentService.purchaseCourse(userId, courseId, amount.doubleValue(), paymentMethod);
    }

    @MutationMapping
    public Payment purchaseSubscription(@Argument Long userId, @Argument Float amount, @Argument String paymentMethod) {
        // throw new NotImplementedException();
        return enrollmentService.purchaseSubscription(userId, amount.doubleValue(), paymentMethod);
    }
}
