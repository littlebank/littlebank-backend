package com.littlebank.finance.domain.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonFriendInfoResponse {
    private Long friendId;
    private String customName;
    private Boolean isBlocked;
    private Boolean isBestFriend;

    public static CommonFriendInfoResponse ofMe() {
        return new CommonFriendInfoResponse(
                null,
                "나",
                false,
                false
        );
    }
}
