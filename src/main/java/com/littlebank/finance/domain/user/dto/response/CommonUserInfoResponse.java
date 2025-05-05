package com.littlebank.finance.domain.user.dto.response;

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
    private String statusMessage;
    private String profileImagePath;
    private UserRole role;
}
