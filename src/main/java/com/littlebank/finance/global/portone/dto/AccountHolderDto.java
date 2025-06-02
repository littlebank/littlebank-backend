package com.littlebank.finance.global.portone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class AccountHolderDto {
    private String bankHolder;

    public static AccountHolderDto of(Map<String, Object> data) {
        return AccountHolderDto.builder()
                .bankHolder((String) data.get("bank_holder"))
                .build();
    }
}
