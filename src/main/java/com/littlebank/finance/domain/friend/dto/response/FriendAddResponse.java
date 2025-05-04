package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendAddResponse {
    private Long friendId;

    public static FriendAddResponse of(Friend friend) {
        return FriendAddResponse.builder()
                .friendId(friend.getId())
                .build();
    }
}
