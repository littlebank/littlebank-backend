package com.littlebank.finance.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountHolderVerifyResponse {
    private String accountHolder;

    public static AccountHolderVerifyResponse of(String name) {
        return AccountHolderVerifyResponse.builder()
                .accountHolder(name)
                .build();
    }
}
