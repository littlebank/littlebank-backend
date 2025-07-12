package com.littlebank.finance.domain.point.dto.response.admin;

import com.littlebank.finance.domain.point.domain.Payment;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentMethod;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChargeHistoryResponse {
    private Long paymentId;
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private TossPaymentMethod paymentMethod;
    private TossPaymentStatus paymentStatus;
    private LocalDateTime paidAt;
    private Long userId;
    private String userEmail;
    private String userName;
    private String userPhone;

    public static ChargeHistoryResponse of(Payment payment) {
        return ChargeHistoryResponse.builder()
                .paymentId(payment.getId())
                .paymentKey(payment.getTossPaymentKey())
                .orderId(payment.getTossOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getTossPaymentMethod())
                .paymentStatus(payment.getTossPaymentStatus())
                .paidAt(payment.getPaidAt())
                .userId(payment.getUser().getId())
                .userEmail(payment.getUser().getEmail())
                .userName(payment.getUser().getName())
                .userPhone(payment.getUser().getPhone())
                .build();
    }
}
