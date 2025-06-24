package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.TrialSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrialSubscriptionRepository extends JpaRepository<TrialSubscription, Long> {
    boolean existsByUserId(Long userId);
    Optional<TrialSubscription> findByUserId(Long userId);
}


