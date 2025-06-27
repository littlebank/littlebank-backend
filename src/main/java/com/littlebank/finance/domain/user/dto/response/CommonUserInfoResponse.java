package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonUserInfoResponse {
    private Long userId;
    private String realName;
    private String rrn;
    private String phone;
    private String statusMessage;
    private String backName;
    private String backCode;
    private String backAccount;
    private String profileImagePath;
    private UserRole role;

    public static CommonUserInfoResponse of(User user) {
        return CommonUserInfoResponse.builder()
                .userId(user.getId())
                .realName(user.getName())
                .rrn(user.getRrn())
                .phone(user.getPhone())
                .statusMessage(user.getStatusMessage())
                .backName(user.getBankName())
                .backCode(user.getBankCode())
                .backAccount(user.getBankAccount())
                .profileImagePath(user.getProfileImagePath())
                .role(user.getRole())
                .build();
    }
}
