package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.domain.TransactionHistory;
import com.littlebank.finance.domain.point.domain.constant.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>, CustomTransactionHistoryRepository {
    boolean existsByRewardTypeAndRewardId(RewardType type, Long goalId);
}
