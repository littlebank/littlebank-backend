package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonPointTransferResponse {
    private Long transactionHistoryId;
    private Integer pointAmount;
    private String message;
    private Integer remainingPoint;
    private Long receiverId;

    public static CommonPointTransferResponse of(TransactionHistory transactionHistory) {
        return CommonPointTransferResponse.builder()
                .transactionHistoryId(transactionHistory.getId())
                .pointAmount(transactionHistory.getPointAmount())
                .message(transactionHistory.getMessage())
                .remainingPoint(transactionHistory.getRemainingPoint())
                .receiverId(transactionHistory.getReceiver().getId())
                .build();
    }
}
