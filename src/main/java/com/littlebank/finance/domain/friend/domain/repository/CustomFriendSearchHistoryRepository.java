package com.littlebank.finance.domain.friend.domain.repository;

import java.util.List;

public interface CustomFriendSearchHistoryRepository {
    void deleteSearchHistoryInIds(Long userId, List<Long> searchHistoryIds);
}
