package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RefundHistoryResponse {
    private Long refundId;
    private Integer requestedAmount;
    private Integer processedAmount;
    private RefundStatus status;
    private LocalDateTime requestedAt;
    private Long depositTargetUserId;
}
