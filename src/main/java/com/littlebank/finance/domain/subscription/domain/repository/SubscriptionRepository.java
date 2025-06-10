package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, CustomSubscriptionRepository {
}
