package com.littlebank.finance.domain.family.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FamilyEnterCheckResponse {
    private Boolean isJoined;

    public static FamilyEnterCheckResponse of(Boolean isJoined) {
        return FamilyEnterCheckResponse.builder()
                .isJoined(isJoined)
                .build();
    }
}
