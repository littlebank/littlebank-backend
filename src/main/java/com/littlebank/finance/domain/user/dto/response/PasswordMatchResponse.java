package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordMatchResponse {
    private String password;

    public static PasswordMatchResponse of(User user) {
        return PasswordMatchResponse.builder()
                .password(user.getPassword())
                .build();
    }
}
