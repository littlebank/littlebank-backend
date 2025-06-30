package com.littlebank.finance.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentHistoryResponse {
    private Long paymentId;
    private Integer chargePoint;
    private Integer remainingPoint;
    private String name;
    private String pgProvider;
    private String payMethod;
    private LocalDateTime paidAt;
}
