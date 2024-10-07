package org.example.enrollmentservice.domain.service;

import org.example.enrollmentservice.domain.entity.Enrollment;
import org.example.enrollmentservice.domain.entity.Subscription;
import org.example.enrollmentservice.domain.repository.EnrollmentRepository;
import org.example.enrollmentservice.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final SubscriptionRepository subscriptionRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, SubscriptionRepository subscriptionRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Enrollment registerCourse(Long userId, Long courseId, Long paymentId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setPaymentId(paymentId);
        enrollment.setRegistrationDate(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    public Subscription manageSubscription(Long userId, LocalDateTime startDate, LocalDateTime endDate, Long paymentId) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setPaymentId(paymentId);
        return subscriptionRepository.save(subscription);
    }

    public Subscription renewSubscription(Long subscriptionId, LocalDateTime newStartDate, LocalDateTime newEndDate) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Subscription not found with id: " + subscriptionId));
        subscription.setStartDate(newStartDate);
        subscription.setEndDate(newEndDate);
        return subscriptionRepository.save(subscription);
    }

    public boolean checkCourseAccess(Long userId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        return enrollment.isPresent();
    }

    public boolean checkSubscriptionAccess(Long userId, LocalDateTime now) {
        Optional<Subscription> subscription = subscriptionRepository.findTopByUserIdAndEndDateAfterOrderByEndDateDesc(userId, now);
        return subscription.isPresent() && !subscription.get().getEndDate().isBefore(now);
    }

    public List<Enrollment> getUserEnrollments(Long userId) {
        return enrollmentRepository.findAllByUserId(userId);
    }

    public List<Subscription> getUserPlanSubscriptions(Long userId) {
        return subscriptionRepository.findAllByUserId(userId);
    }
}
