package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserSearchResponse {
    private Long searchUserId;
    private String email;
    private String name;
    private String statusMessage;
    private String profileImagePath;
    private UserRole role;
    private FriendInfoResponse friendInfo;

    @Getter
    @AllArgsConstructor
    public static class FriendInfoResponse {
        private Long id;
        private String customName;
        private Boolean isBlocked;
        private Boolean isBestFriend;
    }
}
