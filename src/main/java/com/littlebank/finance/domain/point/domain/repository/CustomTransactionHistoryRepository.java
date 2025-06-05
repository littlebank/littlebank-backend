package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.dto.response.LatestSentAccountResponse;
import com.littlebank.finance.domain.point.dto.response.ReceivePointHistoryResponse;
import com.littlebank.finance.domain.point.dto.response.SendPointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTransactionHistoryRepository {
    Page<LatestSentAccountResponse> findLatestSentAccountByUserId(Long userId, Pageable pageable);
    List<ReceivePointHistoryResponse> findReceivedPointHistoryByUserId(Long userId);
    List<SendPointHistoryResponse> findSentPointHistoryByUserId(Long userId);
}
