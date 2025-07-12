package com.littlebank.finance.domain.point.domain.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TossPaymentMethod {

    CARD("카드"),
    ACCOUNT_TRANSFER("계좌이체"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAYMENT("간편결제"),
    MOBILE_PHONE("휴대폰");

    @JsonValue
    private final String value;

    @JsonCreator
    public static TossPaymentMethod fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + value));
    }
}
