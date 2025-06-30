package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.constant.RewardType;
import com.littlebank.finance.domain.point.domain.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonPointTransferResponse {
    private Long historyId;
    private Integer pointAmount;
    private String message;
    private Integer remainingPoint;
    private Long receiverId;
    private RewardType rewardType;
    private Long rewardId;

    public static CommonPointTransferResponse of(TransactionHistory transactionHistory) {
        return CommonPointTransferResponse.builder()
                .historyId(transactionHistory.getId())
                .pointAmount(transactionHistory.getPointAmount())
                .message(transactionHistory.getMessage())
                .remainingPoint(transactionHistory.getSenderRemainingPoint())
                .receiverId(transactionHistory.getReceiver().getId())
                .rewardType(transactionHistory.getRewardType())
                .rewardId(transactionHistory.getRewardId())
                .build();
    }
}
