package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.SendPointHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.WaitStatusRefundResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomRefundRepository {
    Page<WaitStatusRefundResponse> findRefundHistoryByUserId(Long userId, Pageable pageable);
    List<SendPointHistoryResponse> findRefundHistoryByUserId(Long userId);
}
