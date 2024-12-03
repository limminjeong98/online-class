package org.example.classgraphql.service;

import org.example.classgraphql.model.Enrollment;
import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.PlanSubscription;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class DummyEnrollmentService {

    private List<Enrollment> enrollments = new ArrayList<>();
    private List<PlanSubscription> subscriptions = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    private AtomicLong enrollmentIdGenerator = new AtomicLong(100);
    private AtomicLong subscriptionIdGenerator = new AtomicLong(100);
    private AtomicLong paymentIdGenerator = new AtomicLong(100);

    public DummyEnrollmentService() {
        initData();
    }

    private void initData() {
        // Initialize some dummy payments
        Long paymentId1 = paymentIdGenerator.getAndIncrement();
        Long paymentId2 = paymentIdGenerator.getAndIncrement();
        Long paymentId3 = paymentIdGenerator.getAndIncrement();
        Long paymentId4 = paymentIdGenerator.getAndIncrement();

        payments.add(new Payment(paymentId1, 100L, null, "COURSE", 100.00f, "Credit Card", LocalDateTime.now().minusDays(10).toString()));
        payments.add(new Payment(paymentId2, 100L, null, "COURSE", 120.00f, "PayPal", LocalDateTime.now().minusDays(15).toString()));
        payments.add(new Payment(paymentId3, 101L, null, "SUBSCRIPTION", 100.00f, "Credit Card", LocalDateTime.now().minusDays(10).toString()));
        payments.add(new Payment(paymentId4, 102L, null, "SUBSCRIPTION", 120.00f, "PayPal", LocalDateTime.now().minusDays(15).toString()));


        // Initialize some dummy enrollments
        enrollments.add(new Enrollment(enrollmentIdGenerator.getAndIncrement(), 100L, null, 100L, null, paymentId1, null, LocalDateTime.now().minusDays(5).toString()));
        enrollments.add(new Enrollment(enrollmentIdGenerator.getAndIncrement(), 100L, null, 101L, null, paymentId2, null, LocalDateTime.now().minusDays(3).toString()));

        // Initialize some dummy subscriptions
        subscriptions.add(new PlanSubscription(subscriptionIdGenerator.getAndIncrement(), 101L, null, paymentId3, null, LocalDateTime.now().minusDays(300).toString(), LocalDateTime.now().plusDays(65).toString(), "Expired"));
        subscriptions.add(new PlanSubscription(subscriptionIdGenerator.getAndIncrement(), 102L, null, paymentId4, null, LocalDateTime.now().minusDays(200).toString(), LocalDateTime.now().plusDays(160).toString(), "Expired"));
    }

    public List<Enrollment> getEnrollmentsByUserId(Long userId) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<PlanSubscription> getSubscriptionsByUserId(Long userId) {
        return subscriptions.stream()
                .filter(subscription -> subscription.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Payment purchaseCourse(Long userId, Long courseId, Double amount, String paymentMethod) {
        Payment newPayment = new Payment(paymentIdGenerator.getAndIncrement(), userId, null, "COURSE", amount.floatValue(), paymentMethod, LocalDateTime.now().toString());
        payments.add(newPayment);
        return newPayment;
    }

    public Payment purchaseSubscription(Long userId, Double amount, String paymentMethod) {
        Payment newPayment = new Payment(paymentIdGenerator.getAndIncrement(), userId, null, "SUBSCRIPTION", amount.floatValue(), paymentMethod, LocalDateTime.now().toString());
        payments.add(newPayment);
        return newPayment;
    }

    public boolean checkCourseAccess(Long userId, Long courseId) {
        return enrollments.stream().anyMatch(e -> e.getUserId().equals(userId) && e.getCourseId().equals(courseId));
    }

    public boolean checkSubscriptionAccess(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return subscriptions.stream().anyMatch(s -> s.getUserId().equals(userId) && (now.isAfter(LocalDateTime.parse(s.getStartDate())) && now.isBefore(LocalDateTime.parse(s.getEndDate()))));
    }

    public Payment findPaymentById(Long paymentId) {
        return payments.stream().filter(p -> p.getId().equals(paymentId)).findFirst().orElseThrow();
    }
}
