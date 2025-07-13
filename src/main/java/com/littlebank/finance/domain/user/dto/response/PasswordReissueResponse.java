package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordReissueResponse {
    private String email;

    public static PasswordReissueResponse of(User user) {
        return PasswordReissueResponse.builder()
                .email(user.getEmail())
                .build();
    }
}
