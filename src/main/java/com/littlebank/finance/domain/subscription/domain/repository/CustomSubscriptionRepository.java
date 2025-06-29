package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.Subscription;

import java.util.Optional;

public interface CustomSubscriptionRepository {
    Optional<Subscription> findTopByOwnerIdOrderByStartDateDesc(Long userId);
}
