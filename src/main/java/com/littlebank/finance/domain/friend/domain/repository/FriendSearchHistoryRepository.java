package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.domain.FriendSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendSearchHistoryRepository extends JpaRepository<FriendSearchHistory, Long>, CustomFriendSearchHistoryRepository {
    Optional<FriendSearchHistory> findByUserIdAndFriendId(Long userId, Long friendId);
}
