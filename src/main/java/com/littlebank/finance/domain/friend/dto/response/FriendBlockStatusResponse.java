package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendBlockStatusResponse {
    private Long friendId;
    private Boolean isBlocked;

    public static FriendBlockStatusResponse of(Friend friend) {
        return FriendBlockStatusResponse.builder()
                .friendId(friend.getId())
                .isBlocked(friend.getIsBlocked())
                .build();
    }
}
