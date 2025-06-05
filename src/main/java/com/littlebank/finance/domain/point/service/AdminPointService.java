package com.littlebank.finance.domain.point.service;

import com.littlebank.finance.domain.point.domain.repository.RefundRepository;
import com.littlebank.finance.domain.point.dto.response.admin.RefundHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPointService {
    private final RefundRepository refundRepository;

    @Transactional(readOnly = true)
    public List<RefundHistoryResponse> getUsersRefundHistory() {
        return refundRepository.findAllFetchJoinUser().stream()
                .map(r -> RefundHistoryResponse.of(r))
                .collect(Collectors.toList());
    }
}
