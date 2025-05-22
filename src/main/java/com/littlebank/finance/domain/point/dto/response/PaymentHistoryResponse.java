package com.littlebank.finance.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentHistoryResponse {
    private Long paymentId;
    private Integer chargePont;
    private Integer remainingPoint;
    private String pgProvider;
    private String payMethod;
    private LocalDateTime paidAt;
}
