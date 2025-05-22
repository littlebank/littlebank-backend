package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPaymentRepository {
    Page<PaymentHistoryResponse> findHistoryByUserId(Long userId, Pageable pageable);
}
