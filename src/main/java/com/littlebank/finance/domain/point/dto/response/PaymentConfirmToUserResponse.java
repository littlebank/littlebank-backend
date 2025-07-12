package com.littlebank.finance.domain.point.dto.response;

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
public class PaymentConfirmToUserResponse {
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private TossPaymentMethod method;
    private TossPaymentStatus status;
    private LocalDateTime paidAt;

    public static PaymentConfirmToUserResponse of(Payment payment) {
        return PaymentConfirmToUserResponse.builder()
                .paymentKey(payment.getTossPaymentKey())
                .orderId(payment.getTossOrderId())
                .amount(payment.getAmount())
                .method(payment.getTossPaymentMethod())
                .status(payment.getTossPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
