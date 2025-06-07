package com.littlebank.finance.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LatestRefundDepositTargetResponse {
    private Long refundId;
    private Long userId;
    private String userName;
    private String bankName;
    private String bankAccount;
    private LocalDateTime requestedDate;
}
