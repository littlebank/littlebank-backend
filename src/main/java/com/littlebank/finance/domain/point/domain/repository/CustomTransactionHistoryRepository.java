package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomTransactionHistoryRepository {
    Page<ReceivePointHistoryResponse> findReceivedPointHistoryByUserId(Long userId, Pageable pageable);
}
