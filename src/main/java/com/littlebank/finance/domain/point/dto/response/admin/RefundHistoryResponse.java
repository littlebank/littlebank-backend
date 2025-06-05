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
    private Long userId;
    private String email;
    private String userName;
    private String phone;
    private String bankName;
    private String bankAccount;
    private UserRole role;
    private Boolean isSubscribe;

    public static RefundHistoryResponse of(Refund refund) {
        return RefundHistoryResponse.builder()
                .refundId(refund.getId())
                .requestedAmount(refund.getRequestedAmount())
                .processedAmount(refund.getProcessedAmount())
                .status(refund.getStatus())
                .requestedAt(refund.getCreatedDate())
                .userId(refund.getUser().getId())
                .email(refund.getUser().getEmail())
                .userName(refund.getUser().getName())
                .phone(refund.getUser().getPhone())
                .bankName(refund.getUser().getBankName())
                .bankAccount(refund.getUser().getBankAccount())
                .role(refund.getUser().getRole())
                .isSubscribe(refund.getUser().getIsSubscribe())
                .build();
    }
}
