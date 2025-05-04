package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, CustomFriendRepository {
}
