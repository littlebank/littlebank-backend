package com.littlebank.finance.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DuplicatedEmailCheckResponse {
    private String email;

    public static DuplicatedEmailCheckResponse of(String email) {
        return DuplicatedEmailCheckResponse.builder()
                .email(email)
                .build();
    }
}
