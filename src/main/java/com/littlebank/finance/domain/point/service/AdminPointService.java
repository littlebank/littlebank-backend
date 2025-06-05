package com.littlebank.finance.domain.point.service;

import com.littlebank.finance.domain.point.domain.Refund;
import com.littlebank.finance.domain.point.domain.repository.RefundRepository;
import com.littlebank.finance.domain.point.dto.response.admin.RefundHistoryResponse;
import com.littlebank.finance.domain.point.exception.PointException;
import com.littlebank.finance.global.error.exception.ErrorCode;
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

    public void updateProcessedStatus(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new PointException(ErrorCode.REFUND_NOT_FOUND));

        refund.sentMoney();
    }
}
