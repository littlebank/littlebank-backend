package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountPinVerifyResponse {
    private String pin;

    public static AccountPinVerifyResponse of(User user) {
        return AccountPinVerifyResponse.builder()
                .pin(user.getAccountPin())
                .build();
    }
}
