package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.point.domain.Payment;
import com.littlebank.finance.domain.point.domain.PaymentStatus;
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
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public static PaymentInfoSaveResponse of(Payment payment) {
        return PaymentInfoSaveResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
