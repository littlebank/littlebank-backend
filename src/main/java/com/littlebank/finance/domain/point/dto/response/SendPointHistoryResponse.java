package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.constant.RewardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SendPointHistoryResponse {
    private Long historyId;
    private String type; // "SEND" or "REFUND"
    private Integer pointAmount;
    private String message;
    private Integer remainingPoint;
    private Long receiverId;
    private String receiverName;
    private LocalDateTime sentAt;
    private RewardType rewardType;
    private Long rewardId;

}
