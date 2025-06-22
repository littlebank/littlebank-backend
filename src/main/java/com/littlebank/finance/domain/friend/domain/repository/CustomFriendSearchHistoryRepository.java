package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.domain.FriendSearchHistory;

import java.util.List;

public interface CustomFriendSearchHistoryRepository {
    void deleteSearchHistoryInIds(Long userId, List<Long> searchHistoryIds);

    List<FriendSearchHistory> findByUserIdFetchKoinFriend(Long userId);
}
