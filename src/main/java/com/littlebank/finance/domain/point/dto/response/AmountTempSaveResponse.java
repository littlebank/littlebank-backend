package com.littlebank.finance.domain.point.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AmountTempSaveResponse {
    private String orderId;
    private Integer amount;
    private String orderName;
    private String customerEmail;
    private String customerName;

    public static AmountTempSaveResponse of(String orderId, Integer amount, String orderName, User user) {
        return AmountTempSaveResponse.builder()
                .orderId(orderId)
                .amount(amount)
                .orderName(orderName)
                .customerEmail(user.getEmail())
                .customerName(user.getName())
                .build();
    }

}
