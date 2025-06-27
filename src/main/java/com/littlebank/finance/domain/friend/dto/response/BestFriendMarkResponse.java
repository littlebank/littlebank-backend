package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BestFriendMarkResponse {
    private Long friendId;
    private Boolean isBestFriend;

    public static BestFriendMarkResponse of(Friend friend) {
        return BestFriendMarkResponse.builder()
                .friendId(friend.getId())
                .isBestFriend(friend.getIsBestFriend())
                .build();
    }
}
