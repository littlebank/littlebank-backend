package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateStatusMessageResponse {
    private Long userId;
    private String statusMessage;

    public static UpdateStatusMessageResponse of(User user) {
        return UpdateStatusMessageResponse.builder()
                .userId(user.getId())
                .statusMessage(user.getStatusMessage())
                .build();
    }
}
