package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SocialLoginAdditionalInfoResponse {

    private Long userId;
    private String phone;
    private String rrn;
    private UserRole role;

    public static SocialLoginAdditionalInfoResponse of(User user) {
        return SocialLoginAdditionalInfoResponse.builder()
                .userId(user.getId())
                .phone(user.getPhone())
                .rrn(user.getRrn())
                .role(user.getRole())
                .build();
    }
}
