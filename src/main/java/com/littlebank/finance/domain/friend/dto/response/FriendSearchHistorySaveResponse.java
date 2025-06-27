package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.FriendSearchHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendSearchHistorySaveResponse {
    private Long friendSearchHistoryId;
    private Long friendId;

    public static FriendSearchHistorySaveResponse of(FriendSearchHistory friendSearchHistory) {
        return FriendSearchHistorySaveResponse.builder()
                .friendSearchHistoryId(friendSearchHistory.getId())
                .friendId(friendSearchHistory.getFriend().getId())
                .build();
    }
}
