package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.constant.RewardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReceivePointHistoryResponse {
    private Long historyId;
    private String type;
    private Integer pointAmount;
    private String message;
    private Integer remainingPoint;
    private Long senderId;
    private String senderName;
    private LocalDateTime receivedAt;
    private RewardType rewardType;
    private Long rewardId;
}
