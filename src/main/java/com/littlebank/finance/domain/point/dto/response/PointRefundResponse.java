package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.Refund;
import com.littlebank.finance.domain.point.domain.constant.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointRefundResponse {
    private Long refundId;
    private Integer requestedAmount;
    private Integer processedAmount;
    private Integer remainingPoint;
    private RefundStatus status;
    private Long depositTargetUserId;

    public static PointRefundResponse of(Refund refund) {
        return PointRefundResponse.builder()
                .refundId(refund.getId())
                .requestedAmount(refund.getRequestedAmount())
                .processedAmount(refund.getProcessedAmount())
                .remainingPoint(refund.getRemainingPoint())
                .status(refund.getStatus())
                .depositTargetUserId(refund.getDepositTargetUser().getId())
                .build();
    }
}
