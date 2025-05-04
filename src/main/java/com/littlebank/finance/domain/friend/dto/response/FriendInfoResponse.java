package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendInfoResponse {
    private Long friendId;
    private String customName;
    private Boolean isBlocked;
    private UserInfo userInfo;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String realName;
        private String profileImagePath;
        private UserRole role;
    }
}
