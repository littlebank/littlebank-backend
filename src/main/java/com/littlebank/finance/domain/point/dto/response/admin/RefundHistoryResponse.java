package com.littlebank.finance.domain.point.dto.response.admin;

import com.littlebank.finance.domain.point.domain.Refund;
import com.littlebank.finance.domain.point.domain.RefundStatus;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RefundHistoryResponse {
    private Long refundId;
    private Integer requestedAmount;
    private Integer processedAmount;
    private RefundStatus status;
    private LocalDateTime requestedAt;
    private Long applicationUserId;
    private String applicationUserEmail;
    private String applicationUserName;
    private String applicationUserPhone;
    private UserRole applicationUserRole;
    private Long depositTargetUserId;
    private String depositTargetUserName;
    private String depositTargetBankName;
    private String depositTargetBankAccount;
    private Boolean isSubscribe;

    public static RefundHistoryResponse of(Refund refund) {
        return RefundHistoryResponse.builder()
                .refundId(refund.getId())
                .requestedAmount(refund.getRequestedAmount())
                .processedAmount(refund.getProcessedAmount())
                .status(refund.getStatus())
                .requestedAt(refund.getCreatedDate())
                .applicationUserId(refund.getUser().getId())
                .applicationUserEmail(refund.getUser().getEmail())
                .applicationUserName(refund.getUser().getName())
                .applicationUserPhone(refund.getUser().getPhone())
                .depositTargetUserId(refund.getDepositTargetUser().getId())
                .depositTargetUserName(refund.getDepositTargetUser().getName())
                .depositTargetBankName(refund.getDepositTargetUser().getBankName())
                .depositTargetBankAccount(refund.getDepositTargetUser().getBankAccount())
                .applicationUserRole(refund.getUser().getRole())
                .isSubscribe(refund.getUser().getIsSubscribe())
                .build();
    }
}
