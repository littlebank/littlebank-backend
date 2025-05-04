package com.littlebank.finance.domain.friend.domain.repository;

import com.littlebank.finance.domain.friend.dto.response.FriendInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFriendRepository {
    Page<FriendInfoResponse> findFriendsByUserId(Long userId, Pageable pageable);
}
