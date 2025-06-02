package com.littlebank.finance.global.portone.dto;

import com.littlebank.finance.domain.point.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class PortonePaymentDto {
    private String impUid;
    private String merchantUid;
    private Integer amount;
    private PaymentStatus status;
    private String payMethod;
    private String pgProvider;

    public static PortonePaymentDto of(Map<String, Object> data) {
        return PortonePaymentDto.builder()
                .impUid((String) data.get("imp_uid"))
                .merchantUid((String) data.get("merchant_uid"))
                .amount(((Number) data.get("amount")).intValue())
                .status(PaymentStatus.toPayMentStatus((String) data.get("status")))
                .payMethod((String) data.get("pay_method"))
                .pgProvider((String) data.get("pg_provider"))
                .build();
    }
}
