package org.example.enrollmentservice.domain.repository;

import org.example.enrollmentservice.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findTopByUserIdAndEndDateAfterOrderByEndDateDesc(Long userId, LocalDateTime now);
    List<Subscription> findAllByUserId(Long userId);
}
