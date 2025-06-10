package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserEmailDto {
    private Long userId;
    private String email;

    public static UserEmailDto of(User user) {
        return UserEmailDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }
}
