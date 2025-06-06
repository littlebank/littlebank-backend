package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountPinResetResponse {
    private String pin;

    public static AccountPinResetResponse of(User user) {
        return AccountPinResetResponse.builder()
                .pin(user.getAccountPin())
                .build();
    }
}
