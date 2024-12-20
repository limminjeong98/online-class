package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.PlanSubscription;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.EnrollmentService;
import org.example.classgraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PlanSubscriptionDataResolver {
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public PlanSubscriptionDataResolver(UserService userService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    @SchemaMapping(typeName = "PlanSubscription", field = "user")
    public User getUser(PlanSubscription planSubscription) {
        return userService.findById(planSubscription.getUserId()).orElseThrow();
    }

    @SchemaMapping(typeName = "PlanSubscription", field = "payment")
    public Payment getPayment(PlanSubscription planSubscription) {
        return enrollmentService.findPaymentById(planSubscription.getPaymentId());
    }
}
