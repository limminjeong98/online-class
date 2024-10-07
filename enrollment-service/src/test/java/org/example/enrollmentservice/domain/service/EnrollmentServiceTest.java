package org.example.enrollmentservice.domain.service;

import org.example.enrollmentservice.domain.entity.Enrollment;
import org.example.enrollmentservice.domain.entity.Subscription;
import org.example.enrollmentservice.domain.repository.EnrollmentRepository;
import org.example.enrollmentservice.domain.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentService sut;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Test
    void testRegisterCourse() {
        // given
        Long userId = 1L;
        Long courseId = 100L;
        Long paymentId = 200L;

        Enrollment mockEnrollment = new Enrollment();
        mockEnrollment.setUserId(userId);
        mockEnrollment.setCourseId(courseId);
        mockEnrollment.setPaymentId(paymentId);
        mockEnrollment.setRegistrationDate(LocalDateTime.now());

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(mockEnrollment);

        // when
        Enrollment result = sut.registerCourse(userId, courseId, paymentId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(courseId, result.getCourseId());
        assertEquals(paymentId, result.getPaymentId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void testManageSubscription() {
        // given
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(30);
        Long paymentId = 100L;

        Subscription mockSubscription = new Subscription();
        mockSubscription.setUserId(userId);
        mockSubscription.setStartDate(startDate);
        mockSubscription.setEndDate(endDate);
        mockSubscription.setPaymentId(paymentId);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(mockSubscription);

        // when
        Subscription result = sut.manageSubscription(userId, startDate, endDate, paymentId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        verify(subscriptionRepository).save(any());
    }

    @Test
    void testRenewSubscription() {
        // given
        Long subscriptionId = 300L;
        LocalDateTime newStartDate = LocalDateTime.now();
        LocalDateTime newEndDate = newStartDate.plusDays(30);

        Subscription foundSubscription = new Subscription();
        foundSubscription.setStartDate(LocalDateTime.now().minusDays(15));
        foundSubscription.setEndDate(LocalDateTime.now().minusDays(1));

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(foundSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(foundSubscription);

        // when
        Subscription result = sut.renewSubscription(subscriptionId, newStartDate, newEndDate);

        // then
        assertNotNull(result);
        assertEquals(newStartDate, result.getStartDate());
        assertEquals(newEndDate, result.getEndDate());
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void testRenewSubscription_NotFound() {
        // given
        Long subscriptionId = 300L;

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            sut.renewSubscription(subscriptionId, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        });
    }

    @Test
    void testCheckCourseAccess() {
        // given
        Long userId = 1L;
        Long courseId = 100L;
        when(enrollmentRepository.findByUserIdAndCourseId(userId, courseId)).thenReturn(Optional.of(new Enrollment()));

        // when
        boolean result = sut.checkCourseAccess(userId, courseId);

        // then
        assertTrue(result);
        verify(enrollmentRepository).findByUserIdAndCourseId(userId, courseId);
    }

    @Test
    void testCheckSubscriptionAccess() {
        // given
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = new Subscription();
        subscription.setEndDate(now.plusDays(5));

        when(subscriptionRepository.findTopByUserIdAndEndDateAfterOrderByEndDateDesc(eq(userId), any())).thenReturn(Optional.of(subscription));

        // when
        boolean result = sut.checkSubscriptionAccess(userId, now);

        // then
        assertTrue(result);
        verify(subscriptionRepository).findTopByUserIdAndEndDateAfterOrderByEndDateDesc(userId, now);
    }
}