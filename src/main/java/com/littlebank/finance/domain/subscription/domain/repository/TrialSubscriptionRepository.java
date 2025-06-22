package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.Subscription;
import com.littlebank.finance.domain.subscription.domain.TrialSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrialSubscriptionRepository extends JpaRepository<TrialSubscription, Long> {
    boolean existsByUserId(Long userId);
}


