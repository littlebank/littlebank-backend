package com.littlebank.finance.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReceivePointHistoryResponse {
    private Long historyId;
    private Integer pointAmount;
    private String message;
    private Integer remainingPoint;
    private Long senderId;
    private String senderName;
    private LocalDateTime receivedAt;
}
