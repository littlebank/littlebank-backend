package com.littlebank.finance.global.toss.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.littlebank.finance.domain.point.domain.constant.TossPaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PaymentConfirmResponse {
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private String method;
    private TossPaymentStatus status;
    private OffsetDateTime approvedAt;
}
