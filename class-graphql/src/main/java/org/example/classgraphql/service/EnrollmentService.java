package org.example.classgraphql.service;

import com.example.enrollmentservice.domain.service.EnrollmentServiceGrpc;
import com.example.enrollmentservice.domain.service.EnrollmentServiceOuterClass;
import com.example.enrollmentservice.domain.service.FakePaymentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.classgraphql.model.Enrollment;
import org.example.classgraphql.model.Payment;
import org.example.classgraphql.model.PlanSubscription;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrollmentService {

    @GrpcClient("enrollment-service")
    private EnrollmentServiceGrpc.EnrollmentServiceBlockingStub enrollmentServiceStub;

    @GrpcClient("payment-service")
    private FakePaymentServiceGrpc.FakePaymentServiceBlockingStub fakePaymentServiceStub;

    public Payment purchaseCourse(long userId, long courseId, double amount, String paymentMethod) {
        Payment paymentResponse = createPayment(userId, "COURSE", amount, paymentMethod);
        registerCourse(userId, courseId, paymentResponse.getId());
        return paymentResponse;
    }

    private EnrollmentServiceOuterClass.CourseRegistrationResponse registerCourse(long userId, long courseId, Long paymentId) {
        EnrollmentServiceOuterClass.CourseRegistrationRequest request = EnrollmentServiceOuterClass.CourseRegistrationRequest.newBuilder()
                .setUserId(userId)
                .setCourseId(courseId)
                .setPaymentId(paymentId)
                .build();

        return enrollmentServiceStub.registerCourse(request);
    }

    public Payment purchaseSubscription(long userId, double amount, String paymentMethod) {
        Payment paymentResponse = createPayment(userId, "SUBSCRIPTION", amount, paymentMethod);
        manageSubscription(userId, System.currentTimeMillis(), System.currentTimeMillis() + 31536000000L, paymentResponse.getId()); // 1 year subscription
        return paymentResponse;
    }

    private EnrollmentServiceOuterClass.SubscriptionResponse manageSubscription(long userId, long startDate, long endDate, Long paymentId) {
        EnrollmentServiceOuterClass.SubscriptionRequest request = EnrollmentServiceOuterClass.SubscriptionRequest.newBuilder()
                .setUserId(userId)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setPaymentId(paymentId)
                .build();

        return enrollmentServiceStub.manageSubscription(request);
    }

    private Payment createPayment(long userId, String type, double amount, String paymentMethod) {
        EnrollmentServiceOuterClass.PaymentRequest request = EnrollmentServiceOuterClass.PaymentRequest.newBuilder()
                .setUserId(userId)
                .setType(type)
                .setAmount(amount)
                .setPaymentMethod(paymentMethod)
                .build();

        EnrollmentServiceOuterClass.PaymentResponse response = fakePaymentServiceStub.createPayment(request);
        return Payment.fromProto(response);
    }

    public List<PlanSubscription> getSubscriptionsByUserId(Long userId) {
        EnrollmentServiceOuterClass.UserSubscriptionsRequest request = EnrollmentServiceOuterClass.UserSubscriptionsRequest.newBuilder()
                .setUserId(userId)
                .build();

        EnrollmentServiceOuterClass.UserSubscriptionsResponse response = enrollmentServiceStub.getUserPlanSubscriptions(request);

        return response.getSubscriptionsList().stream()
                .map(PlanSubscription::fromProto)
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsByUserId(Long userId) {
        EnrollmentServiceOuterClass.UserEnrollmentsRequest request = EnrollmentServiceOuterClass.UserEnrollmentsRequest.newBuilder()
                .setUserId(userId)
                .build();

        EnrollmentServiceOuterClass.UserEnrollmentsResponse response = enrollmentServiceStub.getUserEnrollments(request);
        return response.getEnrollmentsList().stream()
                .map(Enrollment::fromProto)
                .collect(Collectors.toList());
    }

    public boolean checkSubscriptionAccess(Long userId) {
        EnrollmentServiceOuterClass.SubscriptionAccessRequest request = EnrollmentServiceOuterClass.SubscriptionAccessRequest.newBuilder()
                .setUserId(userId)
                .build();

        EnrollmentServiceOuterClass.SubscriptionAccessResponse response = enrollmentServiceStub.checkSubscriptionAccess(request);
        return response.getHasAccess();
    }

    public boolean checkCourseAccess(Long courseId, Long userId) {
        EnrollmentServiceOuterClass.CourseAccessRequest request = EnrollmentServiceOuterClass.CourseAccessRequest.newBuilder()
                .setCourseId(courseId)
                .setUserId(userId)
                .build();

        EnrollmentServiceOuterClass.CourseAccessResponse response = enrollmentServiceStub.checkCourseAccess(request);
        return response.getHasAccess();
    }

    public Payment findPaymentById(Long paymentId) {
        EnrollmentServiceOuterClass.PaymentsByIdRequest request = EnrollmentServiceOuterClass.PaymentsByIdRequest.newBuilder()
                .setPaymentId(paymentId)
                .build();

        EnrollmentServiceOuterClass.PaymentsByIdResponse response = fakePaymentServiceStub.getPaymentsByPaymentId(request);
        return Payment.fromProto(response.getPayment());
    }
}
