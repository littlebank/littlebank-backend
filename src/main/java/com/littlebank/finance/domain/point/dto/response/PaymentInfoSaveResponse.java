package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.Payment;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PaymentInfoSaveResponse {
    private Long paymentId;
    private Integer amount;
    private TossPaymentStatus status;
    private LocalDateTime paidAt;

    public static PaymentInfoSaveResponse of(Payment payment) {
        return PaymentInfoSaveResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getTossPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
