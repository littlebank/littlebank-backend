package com.littlebank.finance.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFriendInfoDto {
    private Long userId;
    private String name;
    private String profileImageUrl;
    private Boolean isFriend;
    private Long friendId;
    private String customName;
    private Boolean isBestFriend;
    private Boolean isBlocked;
}
