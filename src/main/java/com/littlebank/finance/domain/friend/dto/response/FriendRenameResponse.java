package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.Friend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendRenameResponse {
    private Long friendId;
    private String changedName;

    public static FriendRenameResponse of(Friend friend) {
        return FriendRenameResponse.builder()
                .friendId(friend.getId())
                .changedName(friend.getCustomName())
                .build();
    }
}
