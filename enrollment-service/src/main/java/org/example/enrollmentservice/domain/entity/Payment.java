package org.example.enrollmentservice.domain.entity;

import com.example.enrollmentservice.domain.service.EnrollmentServiceOuterClass;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    public EnrollmentServiceOuterClass.PaymentResponse toProto() {
        EnrollmentServiceOuterClass.PaymentResponse.Builder builder = EnrollmentServiceOuterClass.PaymentResponse.newBuilder();

        if (this.id != null) {
            builder.setPaymentId(this.id);
            builder.setPaymentSuccessful(this.id != null);
        }
        if (this.userId != null) {
            builder.setUserId(this.userId);
        }
        if (this.paymentType != null) {
            builder.setType(this.paymentType.name());
        }
        if (this.amount != null) {
            builder.setAmount(this.amount.doubleValue());
        }
        if (this.paymentMethod != null) {
            builder.setPaymentMethod(this.paymentMethod);
        }
        if (this.paymentDate != null) {
            builder.setPaymentDate(this.paymentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        return builder.build();
    }
}
