package com.littlebank.finance.domain.friend.dto.response;

import com.littlebank.finance.domain.friend.domain.FriendSearchHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FriendRecentlySearchKeywordResponse {
    private Long searchHistoryId;
    private String keyword;
    private Long friendId;
    private LocalDateTime searchAt;

    public static FriendRecentlySearchKeywordResponse of(FriendSearchHistory searchHistory) {
        return FriendRecentlySearchKeywordResponse.builder()
                .searchHistoryId(searchHistory.getId())
                .keyword(searchHistory.getFriend().getCustomName())
                .friendId(searchHistory.getFriend().getId())
                .searchAt(searchHistory.getSearchAt())
                .build();
    }

}
