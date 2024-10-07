package org.example.enrollmentservice.domain.entity;

import com.example.enrollmentservice.domain.service.EnrollmentServiceOuterClass;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

    public EnrollmentServiceOuterClass.Enrollment toProto() {
        return EnrollmentServiceOuterClass.Enrollment.newBuilder()
                .setEnrollmentId(this.id)
                .setUserId(this.userId)
                .setCourseId(this.courseId)
                .setPaymentId(this.paymentId)
                .setRegistrationDate(this.registrationDate.atZone(ZoneId.systemDefault()).toEpochSecond())
                .build();
    }
}
