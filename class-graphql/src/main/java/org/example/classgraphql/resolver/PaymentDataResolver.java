package org.example.classgraphql.resolver;

import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.User;
import org.example.classgraphql.service.DummyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentDataResolver {

    private final DummyUserService userService;

    @Autowired
    public PaymentDataResolver(DummyUserService userService) {
        this.userService = userService;
    }

    @SchemaMapping(typeName = "Payment", field = "user")
    public User getUser(Payment payment) {
        return userService.findById(payment.getUserId()).orElseThrow();
    }
}
