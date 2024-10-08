package org.example.enrollmentservice.domain.grpc;

import com.example.enrollmentservice.domain.service.EnrollmentServiceOuterClass;
import com.example.enrollmentservice.domain.service.FakePaymentServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.enrollmentservice.domain.entity.Payment;
import org.example.enrollmentservice.domain.entity.PaymentType;
import org.example.enrollmentservice.domain.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@GrpcService
public class PaymentGrpcService extends FakePaymentServiceGrpc.FakePaymentServiceImplBase {

    private final PaymentService paymentService;

    public PaymentGrpcService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void createPayment(EnrollmentServiceOuterClass.PaymentRequest request, StreamObserver<EnrollmentServiceOuterClass.PaymentResponse> responseObserver) {
        try {
            Payment payment = paymentService.createPayment(
                    request.getUserId(),
                    PaymentType.valueOf(request.getType()),
                    BigDecimal.valueOf(request.getAmount()),
                    request.getPaymentMethod()
            );

            responseObserver.onNext(payment.toProto());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void listUserPayments(EnrollmentServiceOuterClass.UserPaymentsRequest request, StreamObserver<EnrollmentServiceOuterClass.UserPaymentsResponse> responseObserver) {
        List<Payment> payments = paymentService.getUserPayments(request.getUserId());
        EnrollmentServiceOuterClass.UserPaymentsResponse response = EnrollmentServiceOuterClass.UserPaymentsResponse.newBuilder()
                .addAllPayments(payments.stream().map(Payment::toProto).toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPaymentsByPaymentId(EnrollmentServiceOuterClass.PaymentsByIdRequest request, StreamObserver<EnrollmentServiceOuterClass.PaymentsByIdResponse> responseObserver) {
        Payment payment = paymentService.getPaymentById(request.getPaymentId());
        EnrollmentServiceOuterClass.PaymentsByIdResponse response = EnrollmentServiceOuterClass.PaymentsByIdResponse.newBuilder()
                .setPayment(payment.toProto())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
