package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.SendPointHistoryResponse;

import java.util.List;

public interface CustomRefundRepository {
    List<SendPointHistoryResponse> findRefundHistoryByUserId(Long userId);
}
