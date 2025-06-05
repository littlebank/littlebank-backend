package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.PaymentHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPaymentRepository {
    Page<PaymentHistoryResponse> findHistoryByUserId(Long userId, Pageable pageable);
    List<ReceivePointHistoryResponse> findPaymentHistoryByUserId(Long userId);
}
