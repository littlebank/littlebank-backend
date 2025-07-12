package com.littlebank.finance.global.toss.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentMethod;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentConfirmResponse {
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private String method;
    private TossPaymentStatus status;
    private OffsetDateTime approvedAt;
}
