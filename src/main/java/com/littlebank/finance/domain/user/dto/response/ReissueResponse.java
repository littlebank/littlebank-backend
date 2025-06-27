package com.littlebank.finance.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReissueResponse {

    private String accessToken;

    public static ReissueResponse of(String accessToken) {
        return ReissueResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
